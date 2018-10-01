package by.app.instagram.model.firebase;

import io.realm.RealmObject;

public class HashTagObject extends RealmObject{

    int count_all;
    int count_comments;
    int count_like;
    String name;

    public HashTagObject(){}

    public int getCount_all() {
        return count_all;
    }

    public void setCount_all(int count_all) {
        this.count_all = count_all;
    }

    public int getCount_comments() {
        return count_comments;
    }

    public void setCount_comments(int count_comments) {
        this.count_comments = count_comments;
    }

    public int getCount_like() {
        return count_like;
    }

    public void setCount_like(int count_like) {
        this.count_like = count_like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
