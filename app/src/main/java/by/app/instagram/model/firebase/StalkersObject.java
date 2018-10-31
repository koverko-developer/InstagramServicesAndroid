package by.app.instagram.model.firebase;

import java.util.Comparator;

import io.realm.RealmObject;

public class StalkersObject extends RealmObject{

    int col_comments;
    int col_like;
    String fullname;
    long id;
    String picture;
    String uname;

    public StalkersObject(){}

    public int getCol_comments() {
        return col_comments;
    }

    public void setCol_comments(int col_comments) {
        this.col_comments = col_comments;
    }

    public int getCol_like() {
        return col_like;
    }

    public void setCol_like(int col_like) {
        this.col_like = col_like;
    }

    public String getFull_name() {
        return fullname;
    }

    public void setFull_name(String full_name) {
        this.fullname = full_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public static Comparator<StalkersObject> StalkersComparator = new Comparator<StalkersObject>() {
        @Override
        public int compare(StalkersObject mediaObject, StalkersObject t1) {

            int l1 = mediaObject.col_like;
            int l2 = t1.col_like;

            return Integer.compare(l2, l1);
        }
    };
}
