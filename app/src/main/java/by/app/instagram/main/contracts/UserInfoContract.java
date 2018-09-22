package by.app.instagram.main.contracts;

import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.vk.VKUserInfo;

public class UserInfoContract {

    public interface ViewModel {
        void initCardViewUI();
        void initCardViewMediaUI();
        void initCardViewMedia2UI();
        void setCardViewUI(VKUserInfo viewUI);
        void addCardViewMediaInfo(UserInfoMedia media);
        void addCardViewMedia2Info(UserInfoMedia media);
    }

    public interface Presenter {
        void setUserInfoObserve();
        void getUI();
        void getUserMediaInfo(String next_url);
        void resetInfo();
        void checkMediaRange(Long count_media);
    }

}
