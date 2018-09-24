package by.app.instagram.main.presenters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.UserInfoContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.fui.UserInfo;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.fui.UserInfoTop;
import by.app.instagram.model.realm.TopCommentsR;
import by.app.instagram.model.realm.TopLikersR;
import by.app.instagram.model.realm.UserInfoRealm;
import by.app.instagram.model.realm.UserMediaR;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
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

    private Context context;
    private UserInfoContract.ViewModel _view;
    Prefs prefs;
    int count_feed_media = 0;
    Realm realm;

    private UserInfoMedia userInfoMedia = new UserInfoMedia();

    public UserInfoPresenter(Context context, UserInfoContract.ViewModel _view) {
        this.context = context;
        this._view = _view;
        prefs = new Prefs(context);
        setUserInfoObserve();
        try {
            //Realm.deleteRealm(Realm.getDefaultConfiguration());
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            checkInternet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkInternet() {
        final RealmResults<TopLikersR> topLikes =
              realm.where(TopLikersR.class).findAll();
        if(topLikes != null && topLikes.size() !=0 ) _view.addCardViewMedia3Info(topLikes.get(0).getTopLikers());

        final RealmResults<TopCommentsR> topComments =
                realm.where(TopCommentsR.class).findAll();
        if(topComments != null && topComments.size() !=0 ) _view.addCardViewMedia4Info(topComments.get(0).getTopLikers());

        final RealmResults<UserInfoRealm> userInfo =
                realm.where(UserInfoRealm.class).findAll();
        if(userInfo != null && userInfo.size() != 0) {
            VKUserInfo info = new VKUserInfo();
            Data d = new Data();
            d.setProfilePicture(userInfo.get(0).getProfile());
            Counts counts = new Counts();
            counts.setFollowedBy(userInfo.get(0).getFollowed_by());
            counts.setFollows(userInfo.get(0).getFollows());
            d.setmCounts(counts);
            info.setmData(d);
            _view.setCardViewUI(info);
        }

        final RealmResults<UserMediaR> infoMedia =
                realm.where(UserMediaR.class).findAll();
        if(infoMedia != null && infoMedia.size() !=0 ) {
            UserInfoMedia media = new UserInfoMedia();
            media.setCount_comments(infoMedia.get(0).getCount_comments());
            media.setCount_corousel(infoMedia.get(0).getCount_corousel());
            media.setCount_like(infoMedia.get(0).getCount_like());
            media.setCount_view(infoMedia.get(0).getCount_view());
            media.setCount_video(infoMedia.get(0).getCount_video());
            media.setCount_photo(infoMedia.get(0).getCount_photo());
            _view.addCardViewMediaInfo(media);
            _view.addCardViewMedia2Info(media);
        }
        getUI();

    }

    @Override
    public void setUserInfoObserve() {
    }

    @Override
    public void getUI() {
        _view.showProgress();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", prefs.getLToken());
        Observable<ResponseBody> observable = new ApiServices().getApi().getUserInfo(map);
        observable.subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(data -> {
                      try {
                          String resp = data.string();
                          Gson gson = new Gson();
                          if(resp.contains("error_type")){
                              Meta meta = gson.fromJson(resp, Meta.class);
                              String err = meta.getMeta().getErrorMessage();
                              getUI();
                          }else {
                              VKUserInfo vkUserInfo
                                      = gson.fromJson(resp, VKUserInfo.class);
                              checkMediaRange(vkUserInfo.getmData().getmCounts().getMedia());
                              _view.setCardViewUI(vkUserInfo);
                              addToRealmUI(vkUserInfo);
                              getUserMediaInfo("");
                              getUserInfoTopLikers();
                              getUserInfoTopComments();
                          }

                          String ds = resp;
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  });

    }

    @Override
    public void getUserMediaInfo(String next_url) {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));

        Observable<UserInfoMedia> observable = new ApiServices().getApi().getUserInfoMedia(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->{
                    if(data != null) {
                        try{
                            Gson gson = new Gson();
                            UserInfoMedia media = data;
                            userInfoMedia.addUserMedia(media);
                            Log.e("dsadasdsada", "dsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+String.valueOf(userInfoMedia.count_photo));
                            _view.addCardViewMediaInfo(userInfoMedia);
                            _view.addCardViewMedia2Info(userInfoMedia);
                            addToRealmUI2(media);
                        }
                        catch (Exception e){
                            String d = e.toString();
                        }
                    }
                });

    }

    @Override
    public void resetInfo() {
        userInfoMedia = new UserInfoMedia();
    }

    @Override
    public void checkMediaRange(Long count_media) {
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
        //count_feed_media = 10;

    }

    @Override
    public void getUserInfoTopLikers() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));

        Observable<List<UserInfoTop>> observable = new ApiServices().getApi().getUserTopLikers(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->{

                    if(data != null){
                        _view.addCardViewMedia3Info(data);
                        arrToRealmTopLikers(data);
                    }

                });
    }

    @Override
    public void getUserInfoTopComments() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));

        Observable<List<UserInfoTop>> observable = new ApiServices().getApi().getUserTopComments(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->{
                    if(data != null){
                        _view.hideProgress();
                        _view.addCardViewMedia4Info(data);
                        arrToRealmTopComments(data);
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
}
