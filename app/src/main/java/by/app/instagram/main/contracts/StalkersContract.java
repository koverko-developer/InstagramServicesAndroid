package by.app.instagram.main.contracts;

import java.util.List;

import by.app.instagram.model.firebase.StalkersObject;

public class StalkersContract {

    public interface View{

        void initCardH();
        void initAppBar();
        void initFilter();
        void showProgress();
        void hideProgress();
        void hideFilter();
        void showFilter();
        void showNoInternetConnection();
        void animRotate(android.view.View view, int type);
        void setRecycler(List<StalkersObject> _list);
        void showSnackUpdate();

    }
    public interface Presenter{

        void checkInternet();
        void addProgressListener();
        void destroyProgressListener();
        void getAllData();
        int checkMediaRange(Long count_media);
        void addListenerAll();
        void destroyListenersAll();
        void getFromRealm();
        void addToRealm();

    }

}
