package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import by.app.instagram.api.ApiServices;
import by.app.instagram.contracts.GeneralContract;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.MainContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
import by.app.instagram.model.vk.PrivateUserInfo;
import by.app.instagram.model.vk.VKUserInfo;
import by.app.instagram.workers.MyWorker;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter{

    Context context;
    MainContract.ViewModel view;
    Prefs prefs;

    Handler h;
    boolean isConnect = false;
    boolean isError = false;

    public MainPresenter(Context context, MainContract.ViewModel view) {
        this.context = context;
        this.view = view;
        prefs = new Prefs(context);
        //h = new Handler();
        //h.post(internet);
    }

    Runnable internet = new Runnable() {
        @Override
        public void run() {
            if(!isConnect) checkInternetConnection();
            h.postDelayed(internet, 2000);
        }
    };

    @Override
    public void setLoginUser() {

        view.showProgress();

        Map<String, String> map = new HashMap();
        //map.put("access_token", prefs.getLToken());
        map.put("login", "koverko_dev");
        map.put("pass", "3057686Kowerko1");
        //map.put("access_token", "4234234");
        map.put("cookie", prefs.getLCookie());
        Observable<ResponseBody> observable = new ApiServices().getApi().login(map);
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
                        try {
                            String resp = responseBody.string();
                            Gson gson = new Gson();
                            if(resp.contains("error_type")){
                                Meta meta = gson.fromJson(resp, Meta.class);
                                String err = meta.getMeta().getErrorMessage();
                                //view.checkLogin();
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
                                prefs.setLApi(vkUserInfo.getmData().getId());
                                int c = vkUserInfo.getmData().getmCounts().getFollowedBy().intValue();
                                prefs.setCountFollowers(c);
                                prefs.setCountMedia(vkUserInfo.getmData().getmCounts().getMedia());
                            }
                            view.hideProgress();
                            view.checkLogin();


                            PeriodicWorkRequest request =
                                    new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                                            .build();
                            WorkManager.getInstance().enqueue(request);

                            String ds = resp;
                        } catch (Exception e) {
                            view.hideProgress();
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void checkInternetConnection() {

        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                isConnect = true;
                if(isError) view.connectSucsefull();
                if(!prefs.isLoginApi()) setLoginUser();

            } else {
                view.showNoConnectionMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
