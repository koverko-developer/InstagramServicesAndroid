package by.app.instagram.main.contracts;

import android.support.v7.widget.CardView;
import android.view.View;

import java.util.List;

import by.app.instagram.enums.TypeFeed;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.view.filter.TypeSpinnerFilter;

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
        void setRecycler(List<MediaObject> list);
        int getDateCardPosition();
        int getLieksCardPosition();
        int getCommentsCardPosition();
        int getERCardPosition();
        void initAds();
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
        void sortData();
        void setTypeFeed(TypeFeed _typeFeed);
        void setPeriod1(long _period1);
        void setPeriod2(long _period2);
        void setCountPosts(int _countPosts);
        void setTypeSpinnerFilter(TypeSpinnerFilter _typeSpinnerFilter);
        void checkData();

    }
}
