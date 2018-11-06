package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.UserInfoContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.fui.UserInfo;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.fui.UserInfoTop;
import by.app.instagram.model.realm.TopCommentsR;
import by.app.instagram.model.realm.TopLikersR;
import by.app.instagram.model.realm.UserInfoRealm;
import by.app.instagram.model.realm.UserMediaR;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
import by.app.instagram.model.vk.PrivateUserInfo;
import by.app.instagram.model.vk.VKUserInfo;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserInfoPresenter implements UserInfoContract.Presenter{

    private static String TAG = UserInfoPresenter.class.getName();

    private Context context;
    private UserInfoContract.ViewModel _view;
    Prefs prefs;
    int count_feed_media = 0;
    Realm realm;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference info;
    DatabaseReference topLikers;
    DatabaseReference topComments;

    ValueEventListener listenerProgress;
    ValueEventListener listenerinfo;
    ValueEventListener listenerTopLikers;
    ValueEventListener listenerTopComments;

    private UserInfoMedia userInfoMedia = new UserInfoMedia();

    public UserInfoPresenter(Context context, UserInfoContract.ViewModel _view) {
        this.context = context;
        this._view = _view;
        prefs = new Prefs(context);
        setUserInfoObserve();
        try {
            initRealm();
            checkInternet();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void checkInternet() {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if(!prefs.getUIFirst()) {
                //getUI();
                _view.showSnackUpdate();
            }
            else getUI();

            getFromDB();
            addListenerProgress();
            addListenerInfo();
            addListenerTopLikers();
            addListenerTopComments();

        }else {
            _view.showNoInternetConnection();
            final RealmResults<TopLikersR> topLikes =
                    realm.where(TopLikersR.class).findAll();
            if(topLikes != null && topLikes.size() !=0 ) _view.addCardViewMedia3Info(topLikes.get(topLikes.size() -1).getTopLikers());

            final RealmResults<TopCommentsR> topComments =
                    realm.where(TopCommentsR.class).findAll();
            if(topComments != null && topComments.size() !=0 ) _view.addCardViewMedia4Info(topComments.get(topComments.size()-1).getTopLikers());

            final RealmResults<UserInfoRealm> userInfo =
                    realm.where(UserInfoRealm.class).findAll();
            if(userInfo != null && userInfo.size() != 0) {
                VKUserInfo info = new VKUserInfo();
                Data d = new Data();
                d.setProfilePicture(userInfo.get(userInfo.size()-1).getProfile());
                Counts counts = new Counts();
                counts.setFollowedBy(userInfo.get(userInfo.size()-1).getFollowed_by());
                counts.setFollows(userInfo.get(userInfo.size()-1).getFollows());
                d.setmCounts(counts);
                info.setmData(d);
                _view.setCardViewUI(info);
            }

            final RealmResults<UserMediaR> infoMedia =
                    realm.where(UserMediaR.class).findAll();
            if(infoMedia != null && infoMedia.size() !=0 ) {
                UserInfoMedia media = new UserInfoMedia();
                media.setCount_comments(infoMedia.get(infoMedia.size()-1).getCount_comments());
                media.setCount_corousel(infoMedia.get(infoMedia.size()-1).getCount_corousel());
                media.setCount_like(infoMedia.get(infoMedia.size()-1).getCount_like());
                media.setCount_view(infoMedia.get(infoMedia.size()-1).getCount_view());
                media.setCount_video(infoMedia.get(infoMedia.size()-1).getCount_video());
                media.setCount_photo(infoMedia.get(infoMedia.size()-1).getCount_photo());
                _view.addCardViewMediaInfo(media);
                _view.addCardViewMedia2Info(media);
            }
        }

    }

    @Override
    public void setUserInfoObserve() {
    }

    @Override
    public void getUI() {
        Log.e(TAG, "start gui()");
        _view.showProgress();
        prefs.setUIFirst(false);
        Map<String, String> map = new HashMap<>();
        //map.put("access_token", prefs.getLToken());
        map.put("userId", String.valueOf(prefs.getLApi()));
        Observable<ResponseBody> observable = new ApiServices().getApi().getUserInfo(map);
        observable.subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(data -> {
                      try {
                          Log.e(TAG, "response gui()");
                          String resp = data.string();
                          Gson gson = new Gson();
                          if(resp.contains("error_type")){
                              Meta meta = gson.fromJson(resp, Meta.class);
                              String err = meta.getMeta().getErrorMessage();
                              getUI();
                          }else {

                              PrivateUserInfo info =
                                      gson.fromJson(resp, PrivateUserInfo.class);
                              String sd = "";
                              VKUserInfo vkUserInfo = new VKUserInfo();
                              Data d = new Data();
                              d.setProfilePicture(info.getPicture());
                              prefs.setUIIMG(info.getPicture());
                              prefs.setUIFOLLOWING(info.getFollowingCount());
                              prefs.setUIFOLLOWERS(info.getFollowerCount());
                              d.setFullName(info.getFullName());
                              d.setUsername(info.getUsername());
                              d.setId(String.valueOf(info.getId()));
                              Counts counts = new Counts();
                              counts.setFollows((long) info.getFollowingCount());
                              counts.setFollowedBy((long) info.getFollowerCount());
                              counts.setMedia((long) info.getMediaCount());
                              d.setmCounts(counts);
                              vkUserInfo.setmData(d);

                              checkMediaRange(vkUserInfo.getmData().getmCounts().getMedia());
                              _view.setCardViewUI(vkUserInfo);
                              prefs.setCountMedia(vkUserInfo.getmData().getmCounts().getMedia());
                              prefs.setCountFollowers(vkUserInfo.getmData().getmCounts().getFollowedBy().intValue());
                              addToRealmUI(vkUserInfo);
                              getUserMediaInfo("");

                          }

                          String ds = resp;
                      } catch (Exception e) {
                          e.printStackTrace();

                      }
                  });

    }

    @Override
    public void getUserMediaInfo(String next_url){
//        Gson gson = new Gson();
//        UserInfoMedia media = data;
//        userInfoMedia.addUserMedia(media);
//        Log.e("dsadasdsada", "dsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+String.valueOf(userInfoMedia.count_photo));
//        _view.addCardViewMediaInfo(userInfoMedia);
//        _view.addCardViewMedia2Info(userInfoMedia);
//        addToRealmUI2(media);
        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(checkMediaRange(prefs.getCountMedia())));

        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getUserInfoMedia(String.valueOf(prefs.getLApi()), map);
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
                                    //getUserInfoTopLikers();
                                }

                            }

                        }
                        catch (Exception e){

                        }

                    }
                });

    }

    @Override
    public void resetInfo() {
        userInfoMedia = new UserInfoMedia();
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
    public void getUserInfoTopLikers() {
//        if(data != null){
//            _view.addCardViewMedia3Info(data);
//            arrToRealmTopLikers(data);
//        }
        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));

        Observable<ResponseBody> observable = new ApiServices().getApi().getUserTopLikers(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
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
                                    //_view.hideProgress();
                                }else{
                                    ResponseApi responseApi =
                                            gson.fromJson(resp, ResponseApi.class);

                                    getUserInfoTopComments();

                                }

                            }

                        }
                        catch (Exception e){

                        }

                    }
                });
    }

    @Override
    public void getUserInfoTopComments() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));
//        if(data != null){
//            _view.hideProgress();
//            _view.addCardViewMedia4Info(data);
//            arrToRealmTopComments(data);
//        }
        Observable<ResponseBody> observable = new ApiServices().getApi().getUserTopComments(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
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
                                    //_view.hideProgress();
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
    public void arrToRealmTopLikers(List<UserInfoTop> topList) {

        realm.beginTransaction();

        TopLikersR r = realm.createObject(TopLikersR.class);
        for (UserInfoTop u : topList
             ) {
            final  UserInfoTop top = realm.copyToRealm(u);
            r.getTopLikers().add(top);
        }
        realm.commitTransaction();
    }

    @Override
    public void arrToRealmTopComments(List<UserInfoTop> topList) {
        realm.beginTransaction();

        TopCommentsR r = realm.createObject(TopCommentsR.class);
        for (UserInfoTop u : topList
                ) {
            final  UserInfoTop top = realm.copyToRealm(u);
            r.getTopLikers().add(top);
        }
        realm.commitTransaction();
    }

    @Override
    public void addToRealmUI(VKUserInfo viewUI) {

        realm.beginTransaction();

        UserInfoRealm inf = realm.createObject(UserInfoRealm.class);

        inf.setProfile(viewUI.getmData().getProfilePicture());
        inf.setFollowed_by(viewUI.getmData().getmCounts().getFollowedBy());
        inf.setFollows(viewUI.getmData().getmCounts().getFollows());

        realm.commitTransaction();

    }

    @Override
    public void addToRealmUI2(UserInfoMedia media) {

        realm.beginTransaction();

        UserMediaR inf = realm.createObject(UserMediaR.class);

        inf.setCount_comments(media.getCount_comments());
        inf.setCount_corousel(media.getCount_corousel());
        inf.setCount_like(media.getCount_like());
        inf.setCount_photo(media.getCount_photo());
        inf.setCount_video(media.getCount_video());
        inf.setCount_view(media.getCount_view());

        realm.commitTransaction();

    }

    @Override
    public void addListenerProgress() {
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

        progress = database.getReference("users/"+prefs.getLApi() + "/info/media/progress");
        progress.addValueEventListener(listenerProgress);
    }

    @Override
    public void addListenerInfo() {

        listenerinfo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInfoMedia media = dataSnapshot.getValue(UserInfoMedia.class);

                if(media != null){
                    userInfoMedia = media;
                    _view.addCardViewMediaInfo(userInfoMedia);
                    _view.addCardViewMedia2Info(userInfoMedia);
                    addToRealmUI2(media);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        info = database.getReference("users/"+prefs.getLApi() + "/info/media/all/value/");
        info.addValueEventListener(listenerinfo);

    }

    @Override
    public void addListenerTopLikers() {
        listenerTopLikers = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserInfoTop> userInfoTops = new ArrayList<>();
                for (DataSnapshot data :dataSnapshot.getChildren()
                     ) {
                    UserInfoTop top = data.getValue(UserInfoTop.class);
                    if(top != null) {
                        if(userInfoTops.size() < 5) userInfoTops.add(top);
                    }
                }
                if(userInfoTops.size() != 0)arrToRealmTopLikers(userInfoTops);
                _view.addCardViewMedia3Info(userInfoTops);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        topLikers = database.getReference("users/"+prefs.getLApi() + "/top/likes/value/");
        topLikers.addValueEventListener(listenerTopLikers);
    }

    @Override
    public void addListenerTopComments() {
        listenerTopComments = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<UserInfoTop> userInfoTops = new ArrayList<>();
                for (DataSnapshot data :dataSnapshot.getChildren()
                        ) {
                    UserInfoTop top = data.getValue(UserInfoTop.class);
                    if(top != null) {
                        if(userInfoTops.size() < 5) userInfoTops.add(top);
                    }
                }
                if(userInfoTops.size() != 0)arrToRealmTopComments(userInfoTops);
                _view.addCardViewMedia4Info(userInfoTops);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        topComments = database.getReference("users/"+prefs.getLApi() + "/top/comments/value/");
        topComments.addValueEventListener(listenerTopComments);
    }

    @Override
    public void destroyListener() {
        try {
            progress.removeEventListener(listenerProgress);
            info.removeEventListener(listenerinfo);
            topLikers.removeEventListener(listenerTopLikers);
            topComments.removeEventListener(listenerTopComments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeRealm() {
        realm.close();
    }

    @Override
    public void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void getFromDB() {

        if(prefs.getUIIMG().equals("0")) return;

        _view.setCardUI(prefs.getUIIMG(), (long) prefs.getUIFOLLOWERS(),
                (long) prefs.getUIFOLLOWING());

    }


}
