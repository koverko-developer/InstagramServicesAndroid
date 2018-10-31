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
import by.app.instagram.db.DatesSort;
import by.app.instagram.db.PostFilterSort;
import by.app.instagram.db.PostsDB;
import by.app.instagram.db.Prefs;
import by.app.instagram.enums.TypePosts;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.model.pui.PostsInfo;
import by.app.instagram.model.realm.FeedMediaR;
import by.app.instagram.model.realm.PostInfoR;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
import by.app.instagram.model.vk.PrivateUserInfo;
import by.app.instagram.model.vk.VKUserInfo;
import by.app.instagram.view.filter.TypeSpinnerFilter;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsPresenter implements PostsContract.Presenter{

    private static String TAG = PostsPresenter.class.getName();

    PostsContract.ViewModel _view;
    Context context;

    Prefs prefs;
    PostsDB postsDB;

    int count_feed_media = 0;
    public PostsInfo postInfo = new PostsInfo();
    private TypeSpinnerFilter typeSpinnerFilter = TypeSpinnerFilter.All;

    private int count_posts;
    private long period_1;
    private long period_2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference info;

    ValueEventListener listenerProgress;
    ValueEventListener listenerInfo;

    Realm realm;

    public PostsPresenter(PostsContract.ViewModel _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        postsDB = new PostsDB(context);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        getUI();
    }

    @Override
    public void chechInternet() {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            addListenerInfo();
            addListenerProgress();
            if(!prefs.getPostsFirst()) _view.showSnackUpdate();
            else getUI();
        }
        else {
            _view.showNoInternetConnection();
            getFromRealm();
        }

    }

    @Override
    public void addToRealm() {

        if(postInfo != null){

           postsDB.setPostInfo(postInfo);

        }
    }

    @Override
    public void getFromRealm() {

        PostsInfo value = postsDB.getPostInfo();

        if(value != null && !value.equals("0")){
            if(value.getUserInfoMedia() != null){
                _view.setValueToCardH(value.getUserInfoMedia());
                //_view.addDataToChartLikes(new DatesSort().getDatesFilterAll(data.getChartArr()));
                _view.addToValueToLineView(new DatesSort().getDatesFilterAll(value.getChartArr()));
                postInfo = value;
                if(postInfo != null) addToRealm();
            }
        }

    }

    @Override
    public void getUI() {
        prefs.setPostsFirst(false);
        _view.showProgress();
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
                            prefs.setCountMedia(vkUserInfo.getmData().getmCounts().getMedia());
                            prefs.setCountFollowers(vkUserInfo.getmData().getmCounts().getFollowedBy().intValue());

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

//        PostsInfo data = postsInfo;
//        if(data != null){
//            Log.e(TAG, "set data in cards");
//            _view.hideProgress();
//            if(data.getUserInfoMedia() != null){
//                _view.setValueToCardH(data.getUserInfoMedia());
//                //_view.addDataToChartLikes(new DatesSort().getDatesFilterAll(data.getChartArr()));
//                _view.addToValueToLineView(new DatesSort().getDatesFilterAll(data.getChartArr()));
//                postInfo = data;
//            }
//
//        }

        try {
            Map<String, String> map = new HashMap<>();
            map.put("count_media", String.valueOf(count_feed_media));

            Observable<ResponseBody> observable = new ApiServices().getApi().getPostsInfo(String.valueOf(prefs.getLApi()), map);
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
                          public void onNext(ResponseBody postsInfo) {
                              try{

                                  if(postsInfo != null){
                                      String resp = postsInfo.string();
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
        Log.e(TAG, "count feed medias = "+count_media);

    }

    @Override
    public void sortTypeMedia(TypePosts typePosts) {

        Log.e(TAG, "type spinner = "+ typeSpinnerFilter.toString());
        Log.e(TAG, "count posts = " + count_posts);
        Log.e(TAG, "period 1 = " + period_1);
        Log.e(TAG, "period 2 = " + period_2);

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

            List<ChartItem> chartItemList = postInfo.getChartArr();

            if(typeSpinnerFilter == TypeSpinnerFilter.All){
            }
            else if(typeSpinnerFilter == TypeSpinnerFilter.CurrentMonth) {
                chartItemList = PostFilterSort.getCurrentMonth(postInfo.getChartArr());
            }else if(typeSpinnerFilter == TypeSpinnerFilter.SelectMonth){
                chartItemList = PostFilterSort.getSelectedMonth(postInfo.getChartArr(),
                        period_1, period_2);
            }else if(typeSpinnerFilter == TypeSpinnerFilter.CountPosts){
                chartItemList = PostFilterSort.getCountsPosts(postInfo.getChartArr(), count_posts);
            }

            if(chartItemList != null) {
                for (ChartItem item : chartItemList
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

                if(typePosts == TypePosts.Photo) _view.addToValueToLineView(new DatesSort().getDatesFilterPhoto(chartItemList));
                else if(typePosts == TypePosts.Video) _view.addToValueToLineView(new DatesSort().getDatesFilterVideo(chartItemList));
                else if(typePosts == TypePosts.Slider) _view.addToValueToLineView(new DatesSort().getDatesFilterSlider(chartItemList));
                else _view.addToValueToLineView(new DatesSort().getDatesFilterAll(chartItemList));

            }


        }

    }

    @Override
    public void setTypeSpinnerFilter(TypeSpinnerFilter _typeSpinnerFilter) {
        typeSpinnerFilter = _typeSpinnerFilter;
    }

    @Override
    public void setCountsPosts(int _count) {
        count_posts = _count;
    }

    @Override
    public void setPeriod1(Long _period1) {
        period_1 = _period1;
    }

    @Override
    public void setPeriod2(Long _period2) {
        period_2 = _period2;
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

        progress = database.getReference("users/"+prefs.getLApi() + "/posts/progress");
        progress.addValueEventListener(listenerProgress);
    }

    @Override
    public void addListenerInfo() {
        listenerInfo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator =
//                        new GenericTypeIndicator<Map<String, Object>>();
//                Map<String, Object> value = dataSnapshot.getValue(genericTypeIndicator);

                PostsInfo value = dataSnapshot.getValue(PostsInfo.class);

                if(value != null){
                 if(value.getUserInfoMedia() != null){
                        _view.setValueToCardH(value.getUserInfoMedia());
                        //_view.addDataToChartLikes(new DatesSort().getDatesFilterAll(data.getChartArr()));
                        _view.addToValueToLineView(new DatesSort().getDatesFilterAll(value.getChartArr()));
                        postInfo = value;
                        if(postInfo != null) addToRealm();
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        info = database.getReference("users/"+prefs.getLApi() + "/posts/value");
        info.addValueEventListener(listenerInfo);
    }

    @Override
    public void destroyListeners() {

        try{
            info.removeEventListener(listenerInfo);
            progress.removeEventListener(listenerProgress);
        }catch (Exception e){

        }

    }




}
