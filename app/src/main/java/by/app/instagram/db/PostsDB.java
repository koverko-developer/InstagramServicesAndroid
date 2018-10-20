package by.app.instagram.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import by.app.instagram.model.pui.PostsInfo;

public class PostsDB {

    Context context;
    private static final String APP_PREFERENCES_POSTS = "config";
    private static final String APP_PREFERENCES_media_info = "app_pr_m_i";
    private static final String APP_PREFERENCES_list_charts = "app_pr_l_c";
    private SharedPreferences mSettings;
    private Gson gson;

    public PostsDB(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES_POSTS, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public PostsInfo getPostInfo(){
        String p = mSettings.getString(APP_PREFERENCES_media_info,"0");
        PostsInfo info = gson.fromJson(p, PostsInfo.class);
        return info;
    }

    public void setPostInfo(PostsInfo info){
        String p = gson.toJson(info);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_media_info, p);
        editor.apply();
    }
}
