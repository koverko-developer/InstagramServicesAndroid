package by.app.instagram.model.realm;

import by.app.instagram.model.firebase.HashTagObject;
import io.realm.RealmList;
import io.realm.RealmObject;

public class UsersHashtagsR extends RealmObject{

    RealmList<HashTagObject> allList;
    RealmList<HashTagObject> likesList;
    RealmList<HashTagObject> commentsList;

    public UsersHashtagsR(){}

    public RealmList<HashTagObject> getAllList() {
        return allList;
    }

    public void setAllList(RealmList<HashTagObject> allList) {
        this.allList = allList;
    }

    public RealmList<HashTagObject> getLikesList() {
        return likesList;
    }

    public void setLikesList(RealmList<HashTagObject> likesList) {
        this.likesList = likesList;
    }

    public RealmList<HashTagObject> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(RealmList<HashTagObject> commentsList) {
        this.commentsList = commentsList;
    }
}
