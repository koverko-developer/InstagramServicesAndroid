package by.app.instagram.main.contracts;

import java.util.List;

import by.app.instagram.model.firebase.CategoryObject;

public class PromotionHashtagsContract {

    public interface View{

        void initView();
        void initAppBar();
        void showNoInternetConnection();
        void setRecycler(List<CategoryObject> _list);

    }

    public interface Presenter{

        void checkInternet();
        void addListenerHashtags();
        void generateAllHashtags(List<CategoryObject> _list);

    }

}
