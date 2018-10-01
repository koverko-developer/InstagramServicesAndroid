package by.app.instagram.main.contracts;

import java.util.List;
import java.util.Map;

import by.app.instagram.model.firebase.HashTagObject;

public class UserHashtagContract {

    public interface View {
        void initAppBar();
        void initLists();
        void showProgress();
        void hideProgress();
        void showNoInternetConnection();
        void showSnackUpdate();
        void setRecyclerAll(List<HashTagObject> list);
        void setRecyclerLikes(List<HashTagObject> list);
        void setRecyclerComments(List<HashTagObject> list);
    }

    public interface Presenter {

        void checkInternet();
        void addProgressListener();
        void destroyProgressListener();
        void getAllData();
        int checkMediaRange(Long count_media);
        void addListenerAll();
        void destroyListenersAll();
        void addListenerComments();
        void addListenerLikes();
        void getFromRealm();
        void addToRealm();

    }
}
