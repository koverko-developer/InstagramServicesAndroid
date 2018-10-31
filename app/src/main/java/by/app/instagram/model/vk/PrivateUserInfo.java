package by.app.instagram.model.vk;

import com.google.gson.annotations.SerializedName;

public class PrivateUserInfo {

    @SerializedName("followerCount")
    public int followerCount;

    @SerializedName("followingCount")
    public int followingCount;

    @SerializedName("mediaCount")
    public int mediaCount;

    @SerializedName("picture")
    public String picture;

    @SerializedName("username")
    public String username;

    @SerializedName("fullName")
    public String fullName;

    @SerializedName("id")
    public Long id;

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
