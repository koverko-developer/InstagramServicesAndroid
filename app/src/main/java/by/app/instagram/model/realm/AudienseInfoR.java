package by.app.instagram.model.realm;

import by.app.instagram.model.firebase.AudienceObject;
import io.realm.RealmList;
import io.realm.RealmObject;

public class AudienseInfoR extends RealmObject{

    RealmList<AudienceObject> listFollow;
    RealmList<AudienceObject> listUnFollow;

    public AudienseInfoR(){}

    public RealmList<AudienceObject> getListFollow() {
        return listFollow;
    }

    public void setListFollow(RealmList<AudienceObject> listFollow) {
        this.listFollow = listFollow;
    }

    public RealmList<AudienceObject> getListUnFollow() {
        return listUnFollow;
    }

    public void setListUnFollow(RealmList<AudienceObject> listUnFollow) {
        this.listUnFollow = listUnFollow;
    }
}

