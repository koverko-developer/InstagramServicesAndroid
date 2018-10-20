package by.app.instagram.model.realm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.model.pui.PostsInfo;
import io.realm.RealmList;
import io.realm.RealmObject;

public class PostInfoR extends RealmObject{

    @SerializedName("userInfoMedia")
    public UserInfoMedia userInfoMedia;


    public UserInfoMedia getUserInfoMedia() {
        return userInfoMedia;
    }

    public void setUserInfoMedia(UserInfoMedia userInfoMedia) {
        this.userInfoMedia = userInfoMedia;
    }

}
