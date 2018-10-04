package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.FeedSort;
import by.app.instagram.db.Prefs;
import by.app.instagram.enums.TypeFeed;
import by.app.instagram.main.contracts.FeedContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.realm.AudienseInfoR;
import by.app.instagram.model.realm.FeedMediaR;
import by.app.instagram.model.realm.UsersHashtagsR;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedPresenter implements FeedContract.Prsenter{

    private static String TAG = FeedPresenter.class.getName();

    FeedContract.View _view;
    Context context;
    Prefs prefs;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference feed;

    ValueEventListener listenerProgress;
    ValueEventListener listenerFeed;

    List<MediaObject> feedList = new ArrayList<>();

    Realm realm;
    TypeFeed typeFeed = TypeFeed.Date;
    int position = 0;

    TypeSpinnerFilter typeSpinnerFilter = TypeSpinnerFilter.CountPosts;
    int count_posts = 100;

    long period1 = 0;
    long period2 = 0;

    public FeedPresenter(FeedContract.View _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        checkInernet();
    }


    @Override
    public void checkInernet() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            addLisnenerProgress();
            addListenerFeed();
            if(!prefs.getAudienceFirst()) _view.showSnackUpdate();
            else getAllData();
        }
        else {
            _view.showNoInternetConnection();
            getFromRealm();
        }
    }

    @Override
    public void addLisnenerProgress() {

        listenerProgress = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Progress progress = dataSnapshot.getValue(Progress.class);


                if(progress != null){
                    if(progress.isValue()) _view.showProgress();
                    else _view.hideProgress();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        progress = database.getReference("users/"+prefs.getLApi() + "/feed/progress");
        progress.addValueEventListener(listenerProgress);

    }

    @Override
    public void addListenerFeed() {

        listenerFeed = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                feedList.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    MediaObject object = usersSnapshot.getValue(MediaObject.class);
                    feedList.add(object);
                }

                int i  = feedList.size();
                sortData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        feed = database.getReference("users/"+prefs.getLApi() + "/feed/media/posts/");
        feed.addValueEventListener(listenerFeed);

    }

    @Override
    public void getAllData() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(checkMediaRange(prefs.getCountMedia())));

        prefs.setFeedFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getFeed(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        _view.hideProgress();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        try{

                            if(responseBody != null){
                                String resp = responseBody.string();
                                Gson gson = new Gson();

                                if(resp.contains("error_type")){
                                    Meta meta = gson.fromJson(resp, Meta.class);
                                    String err = meta.getMeta().getErrorMessage();
                                    _view.hideProgress();
                                }else{
                                    ResponseApi responseApi =
                                            gson.fromJson(resp, ResponseApi.class);

                                }

                            }

                        }
                        catch (Exception e){

                        }

                    }
                });

    }

    @Override
    public int checkMediaRange(Long count_media) {
        int count_feed_media = 0;
        if(count_media <=18) count_feed_media = 1;
        else {
            //count_media = Long.valueOf(36);
            double range = (double) count_media / 18;
            if(range > 1) {
                if((range == Math.floor(range)) && !Double.isInfinite(range)){
                    count_feed_media = (int) range;
                }else {
                    count_feed_media = (int) range +1;
                }
            }
        }
        Log.e(TAG, "count media = "+count_feed_media);
        return count_feed_media;
    }

    @Override
    public void destroyListeners() {
        addToRealm();
        progress.removeEventListener(listenerProgress);
        feed.removeEventListener(listenerFeed);

    }

    @Override
    public void addToRealm() {

        if(feedList != null){

            realm.beginTransaction();

            FeedMediaR audienseInfoR = realm.createObject(FeedMediaR.class);

            for (MediaObject object:feedList
                    ) {
                final MediaObject fObject = realm.copyToRealm(object);
                audienseInfoR.getList().add(fObject);
            }

            realm.commitTransaction();
        }

    }

    @Override
    public void getFromRealm() {

        final RealmResults<FeedMediaR> infoRS =
                realm.where(FeedMediaR.class).findAll();

        if(infoRS != null){
            if(infoRS.size() != 0){
                feedList = infoRS.get(infoRS.size() -1).getList();
                sortData();
            }

        }


    }

    @Override
    public void sortData() {
        List<MediaObject> sortList = new ArrayList<>();
        sortList = feedList;
        Log.e(TAG, "period_1 = "+period1);
        Log.e(TAG, "period_2 = "+period2);
        Log.e(TAG, "count posts = "+count_posts);

        // TODO здесь добавить соритровку по ЕR

        if(typeFeed == TypeFeed.Date) {
            Log.e(TAG, "type feed = date");
            position = _view.getDateCardPosition();
            if(position == 0) Collections.sort(sortList, MediaObject.MediaDateComparatorReverse);
            else Collections.sort(sortList, MediaObject.MediaDateComparator);
        }
        else if(typeFeed == TypeFeed.Likes){
            Log.e(TAG, "type feed = likes");
            position = _view.getLieksCardPosition();
            if(position == 0) Collections.sort(sortList, MediaObject.MediaLikeComparatorReverse);
            else Collections.sort(sortList, MediaObject.MediaLikeComparator);
        }else if(typeFeed == TypeFeed.Comments){
            Log.e(TAG, "type feed = comments");
            position = _view.getCommentsCardPosition();
            if(position == 0) Collections.sort(sortList, MediaObject.MediaCommentsComparatorReverse);
            else Collections.sort(sortList, MediaObject.MediaCommentsComparator);
        }

        if(typeSpinnerFilter == TypeSpinnerFilter.CountPosts){
            Log.e(TAG, "type spinner feed = posts");
            if(count_posts <  prefs.getCountMedia()) count_posts = (int) prefs.getCountMedia();
            sortList = FeedSort.sortCountPosts(count_posts, sortList);
        }else if(typeSpinnerFilter == TypeSpinnerFilter.All){
            Log.e(TAG, "type feed = All");
        }else if(typeSpinnerFilter == TypeSpinnerFilter.SelectMonth) {
            Log.e(TAG, "type feed = select month");
            sortList = FeedSort.sortSelectMonth(sortList, period1, period2);
        }
        else if(typeSpinnerFilter == TypeSpinnerFilter.CurrentMonth){
            Log.e(TAG, "type feed = current month");
            sortList = FeedSort.sortCurrentMonth(sortList);
        }

        if(sortList != null) _view.setRecycler(sortList);

    }

    @Override
    public void setTypeFeed(TypeFeed _typeFeed) {

        this.typeFeed = _typeFeed;

    }

    @Override
    public void setPeriod1(long _period1) {
        this.period1 = _period1;
    }

    @Override
    public void setPeriod2(long _period2) {
        this.period2 = _period2;
    }

    @Override
    public void setCountPosts(int _countPosts) {
        this.count_posts = _countPosts;
    }

    @Override
    public void setTypeSpinnerFilter(TypeSpinnerFilter _typeSpinnerFilter) {
        this.typeSpinnerFilter = _typeSpinnerFilter;
    }

    @Override
    public void checkData() {
        if(feedList == null || feedList.size() == 0) checkInernet();
    }
}
