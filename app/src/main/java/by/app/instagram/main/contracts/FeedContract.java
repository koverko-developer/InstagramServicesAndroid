package by.app.instagram.main.contracts;

import android.support.v7.widget.CardView;
import android.view.View;

public class FeedContract {

    public interface View{
        void initCards();
        void initFilter();
        void initAppBar();
        void setSelectedCard(CardView card);
        void animClickCard(android.view.View v);
        void showProgress();
        void hideProgress();
        void hideFilter();
        void showFilter();
        void animRotate(android.view.View view, int type);
        void showNoInternetConnection();
        void showSnackUpdate();
    }

    public interface Prsenter{
        void checkInernet();
        void addLisnenerProgress();
        void addListenerFeed();
        void getAllData();
        int checkMediaRange(Long count_media);
        void destroyListeners();
        void addToRealm();
        void getFromRealm();

    }
}
