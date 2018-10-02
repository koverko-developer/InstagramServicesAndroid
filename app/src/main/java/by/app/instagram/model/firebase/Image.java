package by.app.instagram.model.firebase;

import io.realm.RealmObject;

public class Image extends RealmObject{

    int height;
    int width;
    String url;

    public Image(){};

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
