package by.app.instagram.model.realm;

import io.realm.RealmObject;

public class UserMediaR extends RealmObject{

    int count_like;
    int count_comments;
    int count_view;
    int count_photo;
    int count_video;
    int count_corousel;

    public UserMediaR(){}

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
}
