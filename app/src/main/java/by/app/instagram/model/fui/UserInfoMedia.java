package by.app.instagram.model.fui;

import com.google.gson.annotations.SerializedName;

public class UserInfoMedia {

    @SerializedName("count_like")
    public int count_like;

    @SerializedName("count_comments")
    public int count_comments;

    @SerializedName("count_view")
    public int count_view;

    @SerializedName("count_photo")
    public int count_photo;

    @SerializedName("count_video")
    public int count_video;

    @SerializedName("count_carousel")
    public int count_corousel;

    @SerializedName("next_url")
    public String next_url;


    public int getCount_like() {
        return count_like;
    }

    public void setCount_like(int count_like) {
        this.count_like = count_like;
    }

    public int getCount_comments() {
        return count_comments;
    }

    public void setCount_comments(int count_comments) {
        this.count_comments = count_comments;
    }

    public int getCount_view() {
        return count_view;
    }

    public void setCount_view(int count_view) {
        this.count_view = count_view;
    }

    public int getCount_photo() {
        return count_photo;
    }

    public void setCount_photo(int count_photo) {
        this.count_photo = count_photo;
    }

    public int getCount_video() {
        return count_video;
    }

    public void setCount_video(int count_video) {
        this.count_video = count_video;
    }

    public int getCount_corousel() {
        return count_corousel;
    }

    public void setCount_corousel(int count_corousel) {
        this.count_corousel = count_corousel;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public void addUserMedia(UserInfoMedia media){

        this.count_like = this.count_like + media.getCount_like();
        this.count_comments = this.count_comments + media.getCount_comments();
        this.count_corousel = this.count_corousel + media.getCount_corousel();
        this.count_photo = this.count_photo + media.getCount_photo();
        this.count_video = this.count_video + media.getCount_video();
        this.count_view = this.count_view + media.getCount_view();

    }
}
