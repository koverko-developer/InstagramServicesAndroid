package by.app.instagram.model.realm;

import java.util.List;

import by.app.instagram.model.fui.UserInfoTop;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class TopLikersR extends RealmObject{

    private RealmList<UserInfoTop> topLikers;

    public TopLikersR(){}

    public List<UserInfoTop> getTopLikers() {
        return topLikers;
    }

    public TopLikersR(RealmList<UserInfoTop> topLikers) {
        this.topLikers = topLikers;
    }
}
