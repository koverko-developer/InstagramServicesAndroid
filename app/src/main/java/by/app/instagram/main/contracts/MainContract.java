package by.app.instagram.main.contracts;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import by.app.instagram.enums.TypeMenu;

public interface MainContract {

    interface ViewModel {
        void checkLogin();
        void initLoginFragment();
        void exit();
        void initView();
        void loginApi();
        void checkInternetConnection();
        void showNoConnectionMessage();
        void connectSucsefull();
        void showProgress();
        void hideProgress();
        void initMenu();
        void transactionFragment();
        void clickMenu(TypeMenu _type, TextView _tv, ImageView _img);
        void showMenu();
    }

    interface Presenter {
        void setLoginUser();
        void checkInternetConnection();
    }
}
