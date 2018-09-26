
package by.app.instagram.model.vk;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Counts {

    @SerializedName("followed_by")
    public Long mFollowedBy = (long) 0;
    @SerializedName("follows")
    public Long mFollows = (long) 0;
    @SerializedName("media")
    public Long mMedia;

    public Long getFollowedBy() {
        return mFollowedBy;
    }

    public void setFollowedBy(Long followedBy) {
        mFollowedBy = followedBy;
    }

    public Long getFollows() {
        return mFollows;
    }

    public void setFollows(Long follows) {
        mFollows = follows;
    }

    public Long getMedia() {
        return mMedia;
    }

    public void setMedia(Long media) {
        mMedia = media;
    }

}
