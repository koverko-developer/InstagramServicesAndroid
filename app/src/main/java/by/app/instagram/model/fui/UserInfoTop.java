package by.app.instagram.model.fui;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UserInfoTop extends RealmObject{

    public void UserInfo(){}

    @SerializedName("username")
    public String username;

    @SerializedName("fullname")
    public String fullname;

    @SerializedName("profile_picture")
    public String profile_picture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
