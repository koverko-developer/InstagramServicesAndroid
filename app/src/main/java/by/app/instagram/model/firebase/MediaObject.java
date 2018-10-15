package by.app.instagram.model.firebase;

import java.util.ArrayList;
import java.util.Comparator;

import by.app.instagram.db.Prefs;
import io.realm.RealmList;
import io.realm.RealmObject;

public class MediaObject extends RealmObject{

    int countComments;
    int countViews;
    int countsLikes;
    int mediaType;
    long takenAt;
    String text;
    double er;

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

    public static Comparator<MediaObject> MediaDateComparator = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            Long l1 = mediaObject.takenAt;
            Long l2 = t1.takenAt;

            return l1.compareTo(l2);
        }
    };

    public static Comparator<MediaObject> MediaDateComparatorReverse = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            Long l1 = mediaObject.takenAt;
            Long l2 = t1.takenAt;

            return l2.compareTo(l1);
        }
    };

    public static Comparator<MediaObject> MediaLikeComparatorReverse = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            int l1 = mediaObject.countsLikes;
            int l2 = t1.countsLikes;

            return Integer.compare(l2, l1);
        }
    };

    public static Comparator<MediaObject> MediaLikeComparator = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            int l1 = mediaObject.countsLikes;
            int l2 = t1.countsLikes;

            return Integer.compare(l1, l2);
        }
    };

    public static Comparator<MediaObject> MediaCommentsComparatorReverse = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            int l1 = mediaObject.countComments;
            int l2 = t1.countComments;

            return Integer.compare(l2, l1);
        }
    };

    public static Comparator<MediaObject> MediaCommentsComparator = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            int l1 = mediaObject.countComments;
            int l2 = t1.countComments;

            return Integer.compare(l1, l2);
        }
    };

    public static Comparator<MediaObject> MediaERComparatorReverse = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            double l1 = mediaObject.er;
            double l2 = t1.er;

            return Double.compare(l2, l1);
        }
    };

    public static Comparator<MediaObject> MediaERComparator = new Comparator<MediaObject>() {
        @Override
        public int compare(MediaObject mediaObject, MediaObject t1) {

            double l1 = mediaObject.er;
            double l2 = t1.er;

            return Double.compare(l1, l2);
        }
    };

    public double getEr() {

        return er;
    }

    public void setEr(double er) {
        this.er = er;
    }
}
