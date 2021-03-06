package by.app.instagram.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import by.app.instagram.R;
import by.app.instagram.contracts.GeneralContract;
import by.app.instagram.db.Prefs;
import by.app.instagram.enums.TypeMenu;
import by.app.instagram.main.contracts.MainContract;
import by.app.instagram.main.fragments.AudienceFragment;
import by.app.instagram.main.fragments.FeedFragment;
import by.app.instagram.main.fragments.FragmentLogin;
import by.app.instagram.main.fragments.FragmentPosts;
import by.app.instagram.main.fragments.FragmentPromotionHashtags;
import by.app.instagram.main.fragments.StalkersFragment;
import by.app.instagram.main.fragments.UserHashtagFragment;
import by.app.instagram.main.fragments.UserInfoFragment;
import by.app.instagram.main.presenters.MainPresenter;
import by.app.instagram.workers.AlarmReceiver;
import by.app.instagram.workers.WorkerHelper;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainContract.ViewModel, View.OnClickListener {

    private static final String TAG = "Main";
    Context v;
    Toolbar toolbar;
    Fragment fragment = null;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ProgressBar progressBar;
    ImageView img_header;

    private PendingIntent pendingIntent;

    MainPresenter _presenter;

    Prefs prefs;

    RelativeLayout rel_profile, rel_feed, rel_posts, rel_audience, rel_hashtags,
                   rel_stalkers,rel_hashtags_promotion, rel_exit;

    ImageView img_profile,img_feed, img_posts, img_audience, img_hashtags,
              img_stalkers, img_hashtags_promotion, img_exit;

    TextView tv_profile, tv_feed, tv_posts, tv_audience, tv_hashtags,
             tv_stalkers, tv_hashtags_promotion, tv_exit;

    InterstitialAd interstitial;

    private static final int ALARM_REQUEST_CODE = 133;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = this;
        prefs = new Prefs(this);
        progressBar = findViewById(R.id.progress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(_presenter == null) _presenter = new MainPresenter(this, this);

        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

        checkLogin();

        if(!prefs.getLastUpdate().equals("0")){
            SimpleDateFormat df = new SimpleDateFormat("ss:mm:HH dd-MM-yyyy", Locale.getDefault());
            String current_day_and_month = df.format(Long.parseLong(prefs.getLastUpdate()));
            Toast.makeText(this,
                    getResources().getString(R.string.last_update_ui) +
                            current_day_and_month, Toast.LENGTH_SHORT).show();
        }
        //initMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        //checkLogin();
        ads();
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            exit();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                v.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

        } else {
            showNoConnectionMessage();
        }
    }

    @Override
    public void showNoConnectionMessage() {
        Snackbar.make(toolbar, getString(R.string.err_connection_internet), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void connectSucsefull() {
        Snackbar.make(toolbar, getString(R.string.suc_connection_internet), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void initMenu() {

        img_header = (ImageView) findViewById(R.id.img_header);
        Glide.with(img_header.getContext()).load(R.drawable.header_menu).into(img_header);
        rel_profile = (RelativeLayout) findViewById(R.id.rel_profile);
        rel_feed = (RelativeLayout) findViewById(R.id.rel_feed);
        rel_posts = (RelativeLayout) findViewById(R.id.rel_posts);
        rel_audience = (RelativeLayout) findViewById(R.id.rel_audience);
        rel_stalkers = (RelativeLayout) findViewById(R.id.rel_stalkers);
        rel_hashtags = (RelativeLayout) findViewById(R.id.rel_hashtags);
        rel_hashtags_promotion = (RelativeLayout) findViewById(R.id.rel_hashtags_promotion);
        rel_exit = (RelativeLayout) findViewById(R.id.rel_exit);

        img_profile = (ImageView) findViewById(R.id.menu_img_profile);
        img_feed = (ImageView) findViewById(R.id.menu_img_feed);
        img_posts = (ImageView) findViewById(R.id.menu_img_posts);
        img_audience = (ImageView) findViewById(R.id.menu_img_audience);
        img_stalkers = (ImageView) findViewById(R.id.menu_img_stalkers);
        img_hashtags = (ImageView) findViewById(R.id.menu_img_hashtags);
        img_hashtags_promotion = (ImageView) findViewById(R.id.menu_img_hashtags_promotion);
        img_exit = (ImageView) findViewById(R.id.menu_img_exit);

        tv_profile = (TextView) findViewById(R.id.menu_tv_profile);
        tv_feed = (TextView) findViewById(R.id.menu_tv_feed);
        tv_posts = (TextView) findViewById(R.id.menu_tv_posts);
        tv_audience = (TextView) findViewById(R.id.menu_tv_audience);
        tv_stalkers = (TextView) findViewById(R.id.menu_tv_stalkers);
        tv_hashtags = (TextView) findViewById(R.id.menu_tv_hashtags);
        tv_hashtags_promotion = (TextView) findViewById(R.id.menu_tv_hashtags_promotion);
        tv_exit = (TextView) findViewById(R.id.menu_tv_exit);

        rel_profile.setOnClickListener(this);
        rel_feed.setOnClickListener(this);
        rel_posts.setOnClickListener(this);
        rel_audience.setOnClickListener(this);
        rel_stalkers.setOnClickListener(this);
        rel_hashtags.setOnClickListener(this);
        rel_hashtags_promotion.setOnClickListener(this);
        rel_exit.setOnClickListener(this);
    }

    @Override
    public void transactionFragment() {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        toolbar.setVisibility(View.GONE);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void clickMenu(TypeMenu _type, TextView _tv, ImageView _img) {

        TextView[] tv_arr = new TextView[]{tv_profile, tv_feed, tv_posts, tv_audience,
                tv_stalkers, tv_hashtags, tv_exit, tv_hashtags_promotion};
        for (TextView tv : tv_arr){
            tv.setTextColor(getResources().getColor(R.color.text_dark));

        }
        // TODO здесь изменение изображения еще нужно
        _tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        _tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_view));
        _img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_view));

    }

    @Override
    public void showMenu() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void ads() {
        Random random = new Random();
        int i = random.nextInt(4);
        Log.e(TAG, "ads == " + i);
        if(i == 1){
            MobileAds.initialize(this, getResources().getString(R.string.ad_id1));
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.ad_inter1));
            AdRequest adRequesti = new AdRequest.Builder().build();
            interstitial.loadAd(adRequesti);
        }else {
            MobileAds.initialize(this, getResources().getString(R.string.ad_id2));
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.ad_inter2));
            AdRequest adRequesti = new AdRequest.Builder().build();
            interstitial.loadAd(adRequesti);
        }
    }

    @Override
    public void showAds() {
        if (interstitial.isLoaded()) {
            interstitial.show();
            ads();
        }
    }

    @Override
    public void checkLogin() {

//        if(!prefs.isLoginInsta() || !prefs.isLoginPopster()) initLoginFragment();
//        else if(prefs.isLoginApi()) initView();
//        else loginApi();

        if(prefs.isLoginApi()) initView();
        else initLoginFragment();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showAds();
    }

    @Override
    public void initLoginFragment() {

        fragment = new FragmentLogin(MainActivity.this);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void exit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            prefs.clearPrefs();
            checkLogin();
        } else
        {
            Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(MainActivity.this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
            prefs.clearPrefs();
            checkLogin();

        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void initView() {

        try {
            initMenu();
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            fragment = new UserInfoFragment(MainActivity.this);
            //fragment = new FragmentPosts(MainActivity.this);
//            fragment = new AudienceFragment(MainActivity.this);
            //fragment = new UserHashtagFragment(MainActivity.this);
            //fragment = new StalkersFragment(MainActivity.this);
            //fragment = new FeedFragment(MainActivity.this);
            if(fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }

            toolbar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginApi() {

        _presenter.setLoginUser();

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.rel_profile:
                clickMenu(TypeMenu.Profile, tv_profile, img_profile);
                fragment = new UserInfoFragment(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_feed:
                clickMenu(TypeMenu.Feed, tv_feed, img_feed);
                fragment = new FeedFragment(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_posts:
                clickMenu(TypeMenu.Posts, tv_posts, img_posts);
                fragment = new FragmentPosts(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_audience:
                clickMenu(TypeMenu.Audience, tv_audience, img_audience);
                fragment = new AudienceFragment(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_stalkers:
                clickMenu(TypeMenu.Stalkers, tv_stalkers, img_stalkers);
                fragment = new StalkersFragment(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_hashtags:
                clickMenu(TypeMenu.HashtagsUser, tv_hashtags, img_hashtags);
                fragment = new UserHashtagFragment(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_hashtags_promotion:
                clickMenu(TypeMenu.HashtagsPromotion, tv_hashtags_promotion, img_hashtags_promotion);
                fragment = new FragmentPromotionHashtags(MainActivity.this);
                transactionFragment();
                break;
            case R.id.rel_exit:
                exit();
                break;
        }
    }
}
