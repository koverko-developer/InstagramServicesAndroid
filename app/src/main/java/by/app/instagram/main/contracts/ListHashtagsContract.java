package by.app.instagram.main.contracts;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.model.firebase.ItemHashtag;
import by.app.instagram.model.firebase.PodcategoryHashtag;

public class ListHashtagsContract {

    public interface View {

        void initView();
        void setRecycler(List<PodcategoryHashtag> _list);
        void changeLanguage(String _lg);
        void setCountCopy(int col);
        void showEmptyList();
        void showAddDialog();

    }

    public interface Presenter {

        void getMyHashtags();
        void getFavoriteHashtags();
        void getFBHashtabg(String key);
        void addListenerFB();
        void changeLanguage();
        ArrayList<ItemHashtag> stringToArr(String _str);
    }

}
