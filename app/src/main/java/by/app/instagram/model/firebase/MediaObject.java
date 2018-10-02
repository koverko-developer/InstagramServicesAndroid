package by.app.instagram.model.firebase;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MediaObject extends RealmObject{

    int countComments;
    int countViews;
    int countsLikes;
    int mediaType;
    long takenAt;
    String text;

    Image images;

    public MediaObject(){}

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }

    public int getCountViews() {
        return countViews;
    }

    public void setCountViews(int countViews) {
        this.countViews = countViews;
    }

    public int getCountsLikes() {
        return countsLikes;
    }

    public void setCountsLikes(int countsLikes) {
        this.countsLikes = countsLikes;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public long getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(long takenAt) {
        this.takenAt = takenAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }
}
