package by.app.instagram.main.presenters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.UserInfoContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.vk.VKUserInfo;
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

    private UserInfoMedia userInfoMedia = new UserInfoMedia();

    public UserInfoPresenter(Context context, UserInfoContract.ViewModel _view) {
        this.context = context;
        this._view = _view;
        prefs = new Prefs(context);
        setUserInfoObserve();
        getUI();

    }

    @Override
    public void setUserInfoObserve() {
    }

    @Override
    public void getUI() {
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
                              getUserMediaInfo("");
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

    }
}
