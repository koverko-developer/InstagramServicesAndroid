package by.app.instagram.workers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.MainActivity;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
import by.app.instagram.model.vk.PrivateUserInfo;
import by.app.instagram.model.vk.VKUserInfo;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = AlarmReceiver.class.getName();
    Prefs prefs;
    private static final int ALARM_REQUEST_CODE = 133;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "ALARM ALARM ALARM!!!!!!");
        //Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            //MainActivity activity = (MainActivity) context;
            SimpleDateFormat df = new SimpleDateFormat("ss:mm:HH dd-MM-yyyy", Locale.getDefault());


            Calendar cal = Calendar.getInstance();

            //cal.add(Calendar.MINUTE, 1);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            cal.set(Calendar.HOUR_OF_DAY, 7);
            cal.set(Calendar.MINUTE, 0);

            String current_day_and_month = df.format(cal.getTimeInMillis());
            Log.e(TAG, "add day to date current = " + current_day_and_month);

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
            manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

            if (networkInfo != null && networkInfo.isConnected()) {

                prefs = new Prefs(context);
                prefs.setLastUpdate(String.valueOf(System.currentTimeMillis()));

                getUI();
                getAllDataStalkers();
                getPostsInfo();
                getAllDataHashtags();
                getAllDataFeed();
                getAllDataAudience();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUI() {
        Log.e(TAG, "start gui()");
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
                            getUserMediaInfo("");

                        }

                        String ds = resp;
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                });

    }

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

    public void getAllDataStalkers() {
        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(checkMediaRange(prefs.getCountMedia())));

        prefs.setStalkersFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getStalkers(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

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

    public void getPostsInfo() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("count_media", String.valueOf(prefs.getCountMedia()));

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

    public void getAllDataHashtags() {

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

    public void getAllDataFeed() {

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

    public void getAllDataAudience() {
        //_view.showProgress();
        prefs.setAudienceFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getAudience(String.valueOf(prefs.getLApi()));
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

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

}
