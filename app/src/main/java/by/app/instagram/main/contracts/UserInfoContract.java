package by.app.instagram.main.contracts;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.fui.UserInfoTop;
import by.app.instagram.model.vk.VKUserInfo;

public class UserInfoContract {

    public interface ViewModel {
        void initCardViewUI();
        void initCardViewMediaUI();
        void initCardViewMedia2UI();
        void initCardViewMedia3UI();
        void initCardViewMedia4UI();
        void setCardViewUI(VKUserInfo viewUI);
        void addCardViewMediaInfo(UserInfoMedia media);
        void addCardViewMedia2Info(UserInfoMedia media);
        void addCardViewMedia3Info(List<UserInfoTop> list);
        void addCardViewMedia4Info(List<UserInfoTop> list);
        void cardAnimation(View view, Techniques techniques);
        void textAnimation(View v);
        void hideProgress();
        void showProgress();
    }

    public interface Presenter {
        void checkInternet();
        void setUserInfoObserve();
        void getUI();
        void getUserMediaInfo(String next_url);
        void resetInfo();
        int checkMediaRange(Long count_media);
        void getUserInfoTopLikers();
        void getUserInfoTopComments();
        void arrToRealmTopLikers(List<UserInfoTop> topList);
        void arrToRealmTopComments(List<UserInfoTop> topList);
        void addToRealmUI(VKUserInfo viewUI);
        void addToRealmUI2(UserInfoMedia media);
        void addListenerProgress();
        void addListenerInfo();
        void addListenerTopLikers();
        void addListenerTopComments();
        void destroyListener();
    }

}
