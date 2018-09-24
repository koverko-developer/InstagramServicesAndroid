package by.app.instagram.main.presenters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.DatesSort;
import by.app.instagram.db.Prefs;
import by.app.instagram.enums.TypePosts;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.model.pui.PostsInfo;
import by.app.instagram.model.vk.VKUserInfo;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsPresenter implements PostsContract.Presenter{

    private static String TAG = PostsContract.class.getName();

    PostsContract.ViewModel _view;
    Context context;

    Prefs prefs;

    int count_feed_media = 0;
    PostsInfo postInfo = new PostsInfo();

    public PostsPresenter(PostsContract.ViewModel _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        getUI();
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

        try {
            Map<String, String> map = new HashMap<>();
            map.put("count_media", String.valueOf(count_feed_media));

            Observable<PostsInfo> observable = new ApiServices().getApi().getPostsInfo(String.valueOf(prefs.getLApi()), map);
            observable.subscribeOn(Schedulers.newThread())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(new Subscriber<PostsInfo>() {
                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                          }

                          @Override
                          public void onNext(PostsInfo postsInfo) {
                              PostsInfo data = postsInfo;
                              if(data != null){
                                  Log.e(TAG, "set data in cards");
                                  _view.hideProgress();
                                  if(data.getUserInfoMedia() != null){
                                      _view.setValueToCardH(data.getUserInfoMedia());
                                      _view.addDataToChartLikes(new DatesSort().getDatesFilterAll(data.getChartArr()));

                                      postInfo = data;
                                  }

                              }
                          }
                      });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
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
                Log.e(TAG, "count_feed_media="+count_feed_media);
            }
        }
        //count_feed_media = 28;

    }

    @Override
    public void sortTypeMedia(TypePosts typePosts) {

        UserInfoMedia media = new UserInfoMedia();

        int col_comments = 0;
        int col_likes = 0;
        int col_views = 0;

        List<ChartItem> list = new ArrayList<>();

        int typeMedia = -1;
        if(typePosts == TypePosts.Photo) typeMedia = 1;
        else if(typePosts == TypePosts.Video) typeMedia = 2;
        else if(typePosts == TypePosts.Slider) typeMedia = 8;

        if(postInfo != null){

            for (ChartItem item :postInfo.getChartArr()
                 ) {

                if(typeMedia == -1){
                    col_likes += item.getCountsLikes();
                    col_comments += item.getCountComments();
                    col_views += item.getCountViews();
                }else if(item.getMediaType() == typeMedia){
                    col_likes += item.getCountsLikes();
                    col_comments += item.getCountComments();
                    col_views += item.getCountViews();
                }

            }

            media.setCount_view(col_views);
            media.setCount_like(col_likes);
            media.setCount_comments(col_comments);

            _view.setInfoSortTypePost(media);
        }

    }


}
