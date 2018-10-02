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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.FeedContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.realm.AudienseInfoR;
import by.app.instagram.model.realm.FeedMediaR;
import by.app.instagram.model.realm.UsersHashtagsR;
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
            }
        }

    }
}
