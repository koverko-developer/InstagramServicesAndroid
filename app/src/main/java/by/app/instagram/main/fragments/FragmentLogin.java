package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;

import by.app.instagram.R;
import by.app.instagram.contracts.GeneralContract;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.LoginContract;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class FragmentLogin extends Fragment implements LoginContract.ViewModel,GeneralContract{

    Context context;
    View v;
    ProgressBar progressBar;
    Handler h;
    boolean isConnect = false;
    boolean isError = false;
    Prefs prefs;

    @SuppressLint("ValidFragment")
    public FragmentLogin(Context context) {
        this.context = context;
        h = new Handler();
        prefs = new Prefs(context);
    }

    Runnable internet = new Runnable() {
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
        h.post(internet);
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
                    webView.setVisibility(View.GONE);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d(TAG, "All the cookies in a string:" + cookies);
                    prefs.setLInsta("1");
                    prefs.setLCookie(cookie);
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
    public void checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            isConnect = true;
            if(isError) connectSucsefull();
            if(!prefs.isLoginInsta()) initWVLoginInsta();
            else if(!prefs.isLoginPopster()) initWVLoginPopster();
            //initWVLoginPopster();
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
