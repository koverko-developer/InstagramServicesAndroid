package by.app.instagram.model.realm;

import java.util.List;

import by.app.instagram.model.fui.UserInfoTop;
import io.realm.RealmList;
import io.realm.RealmObject;

public class TopCommentsR extends RealmObject{

    private RealmList<UserInfoTop> topLikers;

    public TopCommentsR(){}

    public List<UserInfoTop> getTopLikers() {
        return topLikers;
    }

    public TopCommentsR(RealmList<UserInfoTop> topLikers) {
        this.topLikers = topLikers;
    }
}
