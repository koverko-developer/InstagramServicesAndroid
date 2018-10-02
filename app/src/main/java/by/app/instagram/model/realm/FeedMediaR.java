package by.app.instagram.model.realm;

import by.app.instagram.model.firebase.MediaObject;
import io.realm.RealmList;
import io.realm.RealmObject;

public class FeedMediaR extends RealmObject{

    RealmList<MediaObject> list;

    public FeedMediaR(){}

    public RealmList<MediaObject> getList() {
        return list;
    }

    public void setList(RealmList<MediaObject> list) {
        this.list = list;
    }
}
