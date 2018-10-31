package by.app.instagram.main.presenters;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.ListHashtagsContract;
import by.app.instagram.model.firebase.CategoryObject;
import by.app.instagram.model.firebase.ItemHashtag;
import by.app.instagram.model.firebase.PodcategoryHashtag;

public class ListHashtagPresenter implements ListHashtagsContract.Presenter{

    Context context;
    ListHashtagsContract.View _view;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference hashtags;

    List<PodcategoryHashtag> list = new ArrayList<>();

    String key = "0";
    String language_str = "RU";

    public ListHashtagPresenter(Context context, ListHashtagsContract.View _view) {
        this.context = context;
        this._view = _view;
    }

    @Override
    public void getMyHashtags() {
        Prefs prefs = new Prefs(context);
        Gson gson = new Gson();
        String s = prefs.getAllMy();
        if(!s.equals("0")){
            List<Object> _list = gson.fromJson(s, List.class);
            list.clear();

            for (Object o: _list
                    ) {
                try {

                    String o_s = o.toString();
                    Log.e("list presenter ", o_s.toString());
                    String arr = o_s.substring(o_s.indexOf("arr=") + 4, o_s.length()-1)
                            .split(", cat")[0];

                    String category = o_s.substring(o_s.indexOf("category=") + 9, o_s.length()-1)
                            .split(",")[0];

                    String itemHashtags = o_s.substring(o_s.indexOf("itemHashtags=") + 13, o_s.length()-1)
                            .split(",")[0];

                    String key = o_s.substring(o_s.indexOf("key=") + 4, o_s.length()-1)
                            .split(",")[0];

                    String language = o_s.substring(o_s.indexOf("language=") + 9, o_s.length()-1)
                            .split(",")[0];

                    String name = o_s.substring(o_s.indexOf("language=") + 18, o_s.length()-1);

                    PodcategoryHashtag pd = new PodcategoryHashtag();
                    pd.setKey(key);
                    pd.setItemHashtags(stringToArr(arr));
                    pd.setArr(arr);
                    pd.setCategory(category);
                    pd.setLanguage(language);
                    pd.setName(name);

                    list.add(pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        //list = _list;
        if(list != null)_view.setRecycler(list);
    }

    @Override
    public void getFavoriteHashtags() {

        Prefs prefs = new Prefs(context);
        Gson gson = new Gson();
        String s = prefs.getAllFavorites();
        if(!s.equals("0")){
            List<Object> _list = gson.fromJson(s, List.class);
            list.clear();

            for (Object o: _list
                    ) {
                try {

                    String o_s = o.toString();
                    Log.e("list presenter ", o_s.toString());
                    String arr = o_s.substring(o_s.indexOf("arr=") + 4, o_s.length()-1)
                            .split(", cat")[0];

                    String category = o_s.substring(o_s.indexOf("category=") + 9, o_s.length()-1)
                            .split(",")[0];

                    String itemHashtags = o_s.substring(o_s.indexOf("itemHashtags=") + 13, o_s.length()-1)
                            .split(",")[0];

                    String key = o_s.substring(o_s.indexOf("key=") + 4, o_s.length()-1)
                            .split(",")[0];

                    String language = o_s.substring(o_s.indexOf("language=") + 9, o_s.length()-1)
                            .split(",")[0];

                    String name = o_s.substring(o_s.indexOf("language=") + 18, o_s.length()-1);

                    PodcategoryHashtag pd = new PodcategoryHashtag();
                    pd.setKey(key);
                    pd.setItemHashtags(stringToArr(arr));
                    pd.setArr(arr);
                    pd.setCategory(category);
                    pd.setLanguage(language);
                    pd.setName(name);

                    list.add(pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //list = _list;
        if(list != null)_view.setRecycler(list);

    }

    @Override
    public void getFBHashtabg(String _key) {

        list.clear();
        this.key = _key;
        addListenerFB();

    }

    @Override
    public void addListenerFB() {
        hashtags = database.getReference("promotion/pod_category/");
        hashtags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()
                        ) {

                    PodcategoryHashtag object = ds.getValue(PodcategoryHashtag.class);

                    if(object != null && object.getCategory().equals(key)) {
                        object.setKey(ds.getKey());
                        object.setItemHashtags(stringToArr(object.getArr()));
                        if(object.getLanguage().equals(language_str)) list.add(object);
                    }

                }

                _view.setRecycler(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void changeLanguage() {

        if(language_str.equals("RU")) {
            language_str = "EN";
            _view.changeLanguage("EN");
        }
        else {
            language_str = "RU";
            _view.changeLanguage("RU");
        }

        List<PodcategoryHashtag> _sortList = new ArrayList<>();

        if(list != null){
            for (PodcategoryHashtag item: list
                 ) {
                if(item.getLanguage().equals(language_str)) _sortList.add(item);
            }
            _view.setRecycler(_sortList);
        }

    }

    @Override
    public ArrayList<ItemHashtag> stringToArr(String _str) {

        ArrayList<ItemHashtag> list = new ArrayList<>();

        if(_str == null) return null;

        String[] arr = _str.split(",");
        if(arr.length > 1){

            for(int i = 0; i < arr.length; i++){
                ItemHashtag itemHashtag = new ItemHashtag();
                itemHashtag.setCopy(false);
                itemHashtag.setName(arr[i]);
                list.add(itemHashtag);
            }
            return list;

        }else {
            ItemHashtag itemHashtag = new ItemHashtag();
            itemHashtag.setCopy(false);
            itemHashtag.setName(_str);
            list.add(itemHashtag);
            return list;
        }
    }
}
