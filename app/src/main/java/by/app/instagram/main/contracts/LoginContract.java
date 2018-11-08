package by.app.instagram.main.contracts;

public interface LoginContract {

    interface ViewModel {

        void initWVLoginInsta();
        void initWVLoginPopster();
        void setLogin();
        void setCookie();
        void initView();
        void login(String _sessions, String _userName, String _id,
                   String _urlgen, String _csrftoken, String _mid);
    }

    interface Presenter {

    }
}
