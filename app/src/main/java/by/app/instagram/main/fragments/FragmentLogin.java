package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import by.app.instagram.R;
import by.app.instagram.api.ApiServices;
import by.app.instagram.contracts.GeneralContract;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.LoginContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.firebase.Image;
import by.app.instagram.model.vk.Counts;
import by.app.instagram.model.vk.Data;
import by.app.instagram.model.vk.PrivateUserInfo;
import by.app.instagram.model.vk.VKUserInfo;
import by.app.instagram.workers.AlarmReceiver;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class FragmentLogin extends Fragment implements LoginContract.ViewModel,GeneralContract{
    private static final int ALARM_REQUEST_CODE = 133;
    Context context;
    View v;
    ProgressBar progressBar;
    Handler h;
    boolean isConnect = false;
    boolean isError = false;
    Prefs prefs;
    MainActivity activity;
    ImageView img_logo, img_bg;

    EditText et_login, et_password;
    Button btn_sign;

    @SuppressLint("ValidFragment")
    public FragmentLogin(Context context) {
        this.context = context;
        h = new Handler();
        prefs = new Prefs(context);
        activity = (MainActivity) context;
    }

    Runnable internet = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            if(!isConnect) checkInternetConnection();
            h.postDelayed(internet, 2000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);
        //h.post(internet);

        initView();

        return v;
    }

    @Override
    public void initWVLoginInsta() {

        Log.d(TAG, "init web view");
        progressBar = (ProgressBar) v.findViewById(R.id.progress);

        final WebView webView = (WebView) v.findViewById(R.id.webView);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress();
            }

            public void onPageFinished(WebView view, String url) {

                hideProgress();
                String cookie = CookieManager.getInstance().getCookie(url);
                if (cookie == null || !cookie.contains("sessionid")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d(TAG, "All the cookies in a string:" + cookies);
                    webView.setVisibility(View.VISIBLE);

                } else {
                    CookieSyncManager.getInstance().sync();
                    //webView.setVisibility(View.GONE);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d(TAG, "All the cookies in a string:" + cookies);
                    prefs.setLInsta("1");
                    String sesid = cookie.split("sessionid=")[1].split(";")[0];
                    prefs.setLCookie(sesid);
                    initWVLoginPopster();
                }


            }
        });
        webView.loadUrl("https://instagram.com/accounts/login/?hl-ru");

    }

    @Override
    public void initWVLoginPopster() {

        Log.d(TAG, "init web view");
        progressBar = (ProgressBar) v.findViewById(R.id.progress);

        final WebView webView = (WebView) v.findViewById(R.id.webView);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onPageFinished(WebView view, String url) {

                hideProgress();
                if(url.contains("redirect_uri")){

                }else {
                    if (url == null || !url.contains("access_token")) {
                        checkInternetConnection();
                    } else {
                        prefs.setLPopster("1");
                        prefs.setLToken(url);
                        MainActivity activity = (MainActivity) context;
                        activity.checkLogin();
                    }
                }


            }
        });
        webView.loadUrl("https://instagram.com/accounts/login/?next=/oauth/authorize/?client_id=2316250b2a1c46369db66e93358a63f3%26redirect_uri=https://popsters.ru/app/start%26response_type=token%26scope=public_content");

    }

    @Override
    public void setLogin() {

    }

    @Override
    public void setCookie() {

    }

    @Override
    public void initView() {

        progressBar = (ProgressBar) v.findViewById(R.id.progress_login);
        et_login = (EditText) v.findViewById(R.id.et_login);
        et_password = (EditText) v.findViewById(R.id.et_password);
        btn_sign = (Button) v.findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                checkInternetConnection();
            }
        });

        img_bg = (ImageView) v.findViewById(R.id.img_bg);
        img_logo = (ImageView) v.findViewById(R.id.img_logo);

        Glide.with(v.getContext()).load(R.drawable.login_header).into(img_bg);
        Glide.with(v.getContext()).load(R.drawable.logo_512_512).into(img_logo);

    }

    @Override
    public void login() {

        String login = et_login.getText().toString();
        String pass = et_password.getText().toString();

        if(login.isEmpty()){
            Toast.makeText(context,
                    context.getResources().getString(R.string.empty_login), Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.isEmpty()) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e(TAG, "start login");

        showProgress();
        Map<String, String> map = new HashMap();
        //map.put("access_token", prefs.getLToken());
        map.put("login", login);
        map.put("pass", pass);
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
                        hideProgress();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        hideProgress();
                        try {
                            String resp = responseBody.string();
                            Gson gson = new Gson();
                            if(resp.contains("eror")){
                                //Meta meta = gson.fromJson(resp, Meta.class);
                                //String err = meta.getMeta().getErrorMessage();
                                Toast.makeText(activity,
                                        context.getResources().getString(R.string.error_login_or_password), Toast.LENGTH_SHORT).show();
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
                                activity.checkLogin();

                                Long current = System.currentTimeMillis();

                                SimpleDateFormat df = new SimpleDateFormat("ss:mm:HH dd-MM-yyyy", Locale.getDefault());


                                Calendar cal = Calendar.getInstance();

                                cal.add(Calendar.DAY_OF_YEAR, 1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 1);
                                //cal.add(Calendar.MINUTE, 1);

                                String current_day_and_month = df.format(cal.getTimeInMillis());
                                Log.e(TAG, "add day to date current = " + current_day_and_month);

                                Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, ALARM_REQUEST_CODE, alarmIntent, 0);
                                AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
                                manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds


                            }
                            String ds = resp;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
//            isConnect = true;
//            if(isError) connectSucsefull();
//            if(!prefs.isLoginInsta()) initWVLoginInsta();
//            else if(!prefs.isLoginPopster()) initWVLoginPopster();
            //initWVLoginPopster();
            login();

        } else {
            showNoConnectionMessage();
        }
    }

    @Override
    public void showNoConnectionMessage() {
        Snackbar.make(v, context.getString(R.string.err_connection_internet), Snackbar.LENGTH_LONG).show();
        isError = true;
    }

    @Override
    public void connectSucsefull() {
        Snackbar.make(v, context.getString(R.string.suc_connection_internet), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if(progressBar != null) progressBar.setVisibility(View.GONE);
    }
}
