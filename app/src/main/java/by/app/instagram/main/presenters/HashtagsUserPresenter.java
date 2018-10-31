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
import by.app.instagram.enums.TypeAudience;
import by.app.instagram.main.contracts.UserHashtagContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.HashTagObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.realm.AudienseInfoR;
import by.app.instagram.model.realm.UsersHashtagsR;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HashtagsUserPresenter implements UserHashtagContract.Presenter{

    private static String TAG = HashtagsUserPresenter.class.getName();

    Context context;
    UserHashtagContract.View _view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference allHashtags;
    DatabaseReference likesHashtags;
    DatabaseReference commentsHashtags;

    Prefs prefs;
    ValueEventListener listenerProgress;
    ValueEventListener listenerAll;
    ValueEventListener listenerLikes;
    ValueEventListener listenerComments;

    List<HashTagObject> list_all = new ArrayList<>();
    List<HashTagObject> list_like = new ArrayList<>();
    List<HashTagObject> list_comments = new ArrayList<>();

    Realm realm;
    public HashtagsUserPresenter(Context context, UserHashtagContract.View _view) {
        this.context = context;
        this._view = _view;
        prefs = new Prefs(context);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        checkInternet();
    }

    @Override
    public void checkInternet() {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            addProgressListener();
            addListenerAll();
            addListenerLikes();
            addListenerComments();
            if(!prefs.getHashtagsFirst())_view.showSnackUpdate();
            else getAllData();
        }
        else {
            _view.showNoInternetConnection();
            getFromRealm();
        }
    }

    @Override
    public void addProgressListener() {

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

        progress = database.getReference("users/"+prefs.getLApi() + "/hashtags/progress");
        progress.addValueEventListener(listenerProgress);

    }

    @Override
    public void destroyProgressListener() {

        progress.removeEventListener(listenerProgress);

    }

    @Override
    public void getAllData() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(checkMediaRange(prefs.getCountMedia())));

        prefs.setHashtagsFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getHashtags(String.valueOf(prefs.getLApi()), map);
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
    public void addListenerAll() {
        _view.showProgress();
        listenerAll = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_all.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    HashTagObject object = usersSnapshot.getValue(HashTagObject.class);
                    if(object != null)
                    {
                        if(list_all.size() < 11) list_all.add(object);
                    }

                }
                _view.setRecyclerAll(list_all);
                _view.hideProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        allHashtags = database.getReference("users/"+prefs.getLApi() + "/hashtags/ht_all/value/");
        allHashtags.addValueEventListener(listenerAll);

    }

    @Override
    public void destroyListenersAll() {

        addToRealm();
        allHashtags.removeEventListener(listenerAll);
        likesHashtags.removeEventListener(listenerLikes);
        commentsHashtags.removeEventListener(listenerComments);

    }

    @Override
    public void addListenerLikes() {
        listenerLikes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_like.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    HashTagObject object = usersSnapshot.getValue(HashTagObject.class);
                    if(object != null)
                    {
                        if(list_like.size() < 11) list_like.add(object);
                    }

                }
                _view.setRecyclerLikes(list_like);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        likesHashtags = database.getReference("users/"+prefs.getLApi() + "/hashtags/ht_like/value/");
        likesHashtags.addValueEventListener(listenerLikes);
    }



    @Override
    public void addListenerComments() {
        listenerComments = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_comments.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    HashTagObject object = usersSnapshot.getValue(HashTagObject.class);
                    if(object != null)
                    {
                        if(list_comments.size() < 11) list_comments.add(object);
                    }

                }
                _view.setRecyclerComments(list_comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        commentsHashtags = database.getReference("users/"+prefs.getLApi() + "/hashtags/ht_comments/value/");
        commentsHashtags.addValueEventListener(listenerComments);
    }


    @Override
    public void getFromRealm() {

        final RealmResults<UsersHashtagsR> infoRS =
                realm.where(UsersHashtagsR.class).findAll();

        if(infoRS != null){
            if(infoRS.size() != 0){
                list_all = infoRS.get(infoRS.size() -1).getAllList();
                list_like = infoRS.get(infoRS.size() -1).getLikesList();
                list_comments = infoRS.get(infoRS.size() -1).getCommentsList();

                _view.setRecyclerAll(list_all);
                _view.setRecyclerLikes(list_like);
                _view.setRecyclerComments(list_comments);
            }
        }

    }

    @Override
    public void addToRealm() {

       if(list_all != null){
           realm.beginTransaction();

           UsersHashtagsR audienseInfoR = realm.createObject(UsersHashtagsR.class);

           for (HashTagObject object:list_all
                   ) {
               final HashTagObject fObject = realm.copyToRealm(object);
               audienseInfoR.getAllList().add(fObject);
           }

           for (HashTagObject object:list_like
                   ) {
               final HashTagObject fObject = realm.copyToRealm(object);
               audienseInfoR.getLikesList().add(fObject);
           }

           for (HashTagObject object:list_comments
                   ) {
               final HashTagObject fObject = realm.copyToRealm(object);
               audienseInfoR.getCommentsList().add(fObject);
           }

           realm.commitTransaction();
       }

    }


}
