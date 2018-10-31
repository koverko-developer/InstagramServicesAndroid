package by.app.instagram.model.firebase;

import java.util.ArrayList;

public class PodcategoryHashtag {

    String arr;
    String category;
    String language;
    String name;
    String key;

    ArrayList<ItemHashtag> itemHashtags;

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemHashtag> getItemHashtags() {
        return itemHashtags;
    }

    public void setItemHashtags(ArrayList<ItemHashtag> itemHashtags) {
        this.itemHashtags = itemHashtags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
