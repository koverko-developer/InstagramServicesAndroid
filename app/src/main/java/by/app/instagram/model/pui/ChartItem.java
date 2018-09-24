package by.app.instagram.model.pui;

import com.google.gson.annotations.SerializedName;

import by.app.instagram.model.fui.UserInfoMedia;

public class ChartItem {

    @SerializedName("mediaType")
    public int mediaType;

    @SerializedName("dates")
    public String dates;

    @SerializedName("countsLikes")
    public int countsLikes;

    @SerializedName("countComments")
    public int countComments;

    @SerializedName("countViews")
    public int countViews;

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getCountsLikes() {
        return countsLikes;
    }

    public void setCountsLikes(int countsLikes) {
        this.countsLikes = countsLikes;
    }

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

    public void setAllValue(int _mediaType, String _date, int _countsLikes,
                            int _countComments, int _countViews){

        this.mediaType = _mediaType;
        this.dates = _date;
        this.countsLikes = _countsLikes;
        this.countComments = _countComments;
        this.countViews = _countViews;

    }

    public void addCountLikes(int col){
        this.countsLikes = this.countsLikes + col;
    }

    public void addCountComments(int col){
        this.countComments = this.countComments + col;
    }

    public void addCountViews(int col){
        this.countViews = this.countViews + col;
    }
}
