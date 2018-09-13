package by.app.instagram.db;

import android.content.Context;
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
        return mSettings.getString(APP_PREFERENCES_L_Token,"0");
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

    public boolean isLoginInsta(){

        if(getLInsta() == 0) return false;
        else return true;

    }

    public boolean isLoginPopster(){

        if(getLPopster() == 0) return false;
        else return true;

    }

}
