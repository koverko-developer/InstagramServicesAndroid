package by.app.instagram.main.contracts;

import android.view.View;

import by.app.instagram.model.fui.UserInfoMedia;

public class PostsContract {

    public interface ViewModel {
        void initChart();
        void initCardH();
        void initCardTypeMedia();
        void cardAnimation(View v);
        void setValueToCardH(UserInfoMedia media);
    }

    public interface Presenter {
        void getUI();
        void getPostsInfo();
        void checkMediaRange(Long count_media);
    }

}
