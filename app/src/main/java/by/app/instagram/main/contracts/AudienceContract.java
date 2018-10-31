package by.app.instagram.main.contracts;

import android.support.v7.widget.CardView;
import android.view.View;

import java.util.List;

import by.app.instagram.enums.TypeAudience;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.view.filter.TypeSpinnerFilter;

public class AudienceContract {

    public interface View {
        void initCardH();
        void initAppBar();
        void initFilter();
        void initGraph();
        void initCardList();
        void showProgress();
        void hideProgress();
        void hideFilter();
        void showFilter();
        void showNoInternetConnection();
        void animClickCard(android.view.View v);
        void changeCard(CardView v);
        void setUnfollow(int count, List<AudienceObject> unfollow_list);
        void setfollow(int count, List<AudienceObject> unfollow_list);
        void setRecycler(List<AudienceObject> list);
        void animText(android.view.View v);
        void animRotate(android.view.View view, int type);
        void addToValueToLineView(List<ChartItem> list);
        void setUnfollowsTV(int count);
        void setfollowsTV(int count);
        void showSnackUpdate();
        void initAds();
    }

    public interface Presenter {

        void checkInternet();
        void getFromRealm();
        void getAllData();
        void addListenerFirebase();
        void destroyListenerFirebase();
        void addprogressListener();
        void destroyListenerProgress();
        void clickCard(TypeAudience type);
        void setPeriod1(Long _period1);
        void setPeriod2(Long _perio2);
        void setTypeSpinnerFilter(TypeSpinnerFilter typeSpinnerFilter);
        void setTypeAudience(TypeAudience typeAudience);
        void sortAudience();
        void addToRealmObjects();


    }
}
