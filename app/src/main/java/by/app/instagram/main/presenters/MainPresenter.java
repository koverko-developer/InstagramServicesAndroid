package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.contracts.GeneralContract;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.MainContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.vk.VKUserInfo;
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
        h = new Handler();
        h.post(internet);
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
        map.put("access_token", prefs.getLToken());
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
                                VKUserInfo vkUserInfo
                                        = gson.fromJson(resp, VKUserInfo.class);
                                prefs.setLApi(vkUserInfo.getmData().getId());
                            }
                            view.hideProgress();
                            view.checkLogin();
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
