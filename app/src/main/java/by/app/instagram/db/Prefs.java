package by.app.instagram.db;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;

import java.util.HashSet;
import java.util.Set;

public class Prefs {

    Context context;
    private static final String APP_PREFERENCES = "config";
    private static final String APP_PREFERENCES_L_Insta = "l_insta";
    private static final String APP_PREFERENCES_L_Popster = "l_popster";
    private static final String APP_PREFERENCES_L_Cookie = "l_cookie";
    private static final String APP_PREFERENCES_L_Token = "l_token";
    private static final String APP_PREFERENCES_L_Api = "l_api";
    private static final String APP_PREFERENCES_USER_count_media = "user_count_media";
    private static final String APP_PREFERENCES_USER_count_followers = "user_count_followers";
    private static final String APP_PREFERENCES_AUDIENCE_f = "audience_first";
    private static final String APP_PREFERENCES_HASHTAGS_f = "hashtags_first";
    private static final String APP_PREFERENCES_STALKERS_f = "stalkers_first";
    private static final String APP_PREFERENCES_UI_f = "userinfo_first";
    private static final String APP_PREFERENCES_FEED_f = "feed_first";
    private static final String APP_PREFERENCES_POSTS_f = "posts_first";
    private static final String APP_PREFERENCES_FAVOTITE_HASHTAGS = "favorite_hashtags";
    private static final String APP_PREFERENCES_MY_HASHTAGS = "my_hashtags";
    private static final String APP_PREFERENCES_LAST_UPDATE = "last_update";
    private static final String APP_PREFERENCES_UI_IMG = "ui_img";
    private static final String APP_PREFERENCES_UI_followers = "ui_followers";
    private static final String APP_PREFERENCES_UI_following = "ui_following";
    private SharedPreferences mSettings;

    public Prefs(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public int  getLInsta(){
        return Integer.parseInt(mSettings.getString(APP_PREFERENCES_L_Insta,"0"));
    }

    public int  getLPopster(){
        return Integer.parseInt(mSettings.getString(APP_PREFERENCES_L_Popster,"0"));
    }

    public void setLInsta(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_L_Insta, id);
        editor.apply();
    }

    public void setLPopster(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_L_Popster, id);
        editor.apply();
    }

    public String getLCookie(){
        return mSettings.getString(APP_PREFERENCES_L_Cookie,"0");
    }

    public String  getLToken(){
        return mSettings.getString(APP_PREFERENCES_L_Token,"0").split("=")[1];
    }

    public void setLCookie(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_L_Cookie, id);
        editor.apply();
    }

    public void setLToken(String token){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_L_Token, token);
        editor.apply();
    }

    public void setLApi(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_L_Api, id);
        editor.apply();
    }

    public Long getLApi(){
        return Long.parseLong(mSettings.getString(APP_PREFERENCES_L_Api,"0"));
    }

    public boolean isLoginInsta(){

        if(getLInsta() == 0) return false;
        else return true;

    }

    public boolean isLoginApi(){

        if(getLApi() == 0) return false;
        else return true;

    }

    public boolean isLoginPopster(){

        if(getLPopster() == 0) return false;
        else return true;

    }

    public boolean  getAudienceFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_AUDIENCE_f, true);
    }

    public void setAudienceFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_AUDIENCE_f, b);
        editor.apply();
    }

    public boolean  getHashtagsFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_HASHTAGS_f, true);
    }

    public void setHashtagsFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_HASHTAGS_f, b);
        editor.apply();
    }

    public boolean  getStalkersFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_STALKERS_f, true);
    }

    public void setStalkersFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_STALKERS_f, b);
        editor.apply();
    }

    public boolean  getFeedFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_FEED_f, true);
    }

    public void setUIFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_UI_f, b);
        editor.apply();
    }

    public boolean  getUIFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_UI_f, true);
    }

    public void setPostsFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_POSTS_f, b);
        editor.apply();
    }

    public boolean  getPostsFirst(){
        return mSettings.getBoolean(APP_PREFERENCES_POSTS_f, true);
    }

    public void setFeedFirst(boolean b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_FEED_f, b);
        editor.apply();
    }

    public long getCountMedia(){
        return mSettings.getLong(APP_PREFERENCES_USER_count_media, 1);
    }

    public void setCountMedia(long b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(APP_PREFERENCES_USER_count_media, b);
        editor.apply();
    }

    public int getCountFollowers(){
        return mSettings.getInt(APP_PREFERENCES_USER_count_followers, 1);
    }

    public void setCountFollowers(int b){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_USER_count_followers, b);
        editor.apply();
    }

    public String getAllFavorites(){
        return mSettings.getString(APP_PREFERENCES_FAVOTITE_HASHTAGS, "0");
    }

    public void setFavorites(String _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_FAVOTITE_HASHTAGS, _str);
        editor.apply();
    }

    public String getAllMy(){
        return mSettings.getString(APP_PREFERENCES_MY_HASHTAGS, "0");
    }

    public void setMy(String _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_MY_HASHTAGS, _str);
        editor.apply();
    }

    public String getLastUpdate(){
        return mSettings.getString(APP_PREFERENCES_LAST_UPDATE, "0");
    }

    public void setLastUpdate(String _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LAST_UPDATE, _str);
        editor.apply();
    }
    public void clearPrefs(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.apply();
    }

    public String getUIIMG(){
        return mSettings.getString(APP_PREFERENCES_UI_IMG, "0");
    }

    public void setUIIMG(String _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_UI_IMG, _str);
        editor.apply();
    }

    public int getUIFOLLOWERS(){
        return mSettings.getInt(APP_PREFERENCES_UI_followers, 0);
    }

    public void setUIFOLLOWERS(int _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_UI_followers, _str);
        editor.apply();
    }

    public int getUIFOLLOWING(){
        return mSettings.getInt(APP_PREFERENCES_UI_following, 0);
    }

    public void setUIFOLLOWING(int _str){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_UI_following, _str);
        editor.apply();
    }

}
