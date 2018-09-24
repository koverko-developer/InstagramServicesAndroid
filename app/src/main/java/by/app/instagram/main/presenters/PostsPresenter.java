package by.app.instagram.main.presenters;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.pui.PostsInfo;
import by.app.instagram.model.vk.VKUserInfo;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsPresenter implements PostsContract.Presenter{

    PostsContract.ViewModel _view;
    Context context;

    Prefs prefs;

    int count_feed_media = 0;

    public PostsPresenter(PostsContract.ViewModel _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        getUI();
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
                            getPostsInfo();
                        }

                        String ds = resp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void getPostsInfo() {

        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(count_feed_media));

        Observable<PostsInfo> observable = new ApiServices().getApi().getPostsInfo(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(data ->{
                     if(data != null){
                         if(data.getUserInfoMedia() != null)
                             _view.setValueToCardH(data.getUserInfoMedia());
                     }
                  });
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
