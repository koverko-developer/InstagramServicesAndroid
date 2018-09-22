package by.app.instagram.model.realm;

import io.realm.RealmObject;

public class UserInfoRealm extends RealmObject{

    String profile;
    Long follows;
    Long followed_by;

    public UserInfoRealm(){

    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getFollows() {
        return follows;
    }

    public void setFollows(Long follows) {
        this.follows = follows;
    }

    public Long getFollowed_by() {
        return followed_by;
    }

    public void setFollowed_by(Long followed_by) {
        this.followed_by = followed_by;
    }
}
