package by.app.instagram.main.contracts;

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
    }

    interface Presenter {
        void setLoginUser();
        void checkInternetConnection();
    }
}
