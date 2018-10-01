package by.app.instagram.model.realm;

import by.app.instagram.model.firebase.StalkersObject;
import io.realm.RealmList;
import io.realm.RealmObject;

public class StalkerOnjectR extends RealmObject{

    RealmList<StalkersObject> list;

    public StalkerOnjectR(){};

    public RealmList<StalkersObject> getList() {
        return list;
    }

    public void setList(RealmList<StalkersObject> list) {
        this.list = list;
    }
}
