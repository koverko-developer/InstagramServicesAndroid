package by.app.instagram.main.presenters;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.R;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.PromotionHashtagsContract;
import by.app.instagram.model.firebase.CategoryObject;

public class PromotionHashtagsPresenter implements PromotionHashtagsContract.Presenter {

    PromotionHashtagsContract.View _view;
    Prefs prefs;
    Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference hashtags;

    List<CategoryObject> list = new ArrayList<>();

    public PromotionHashtagsPresenter(PromotionHashtagsContract.View _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        checkInternet();
    }


    @Override
    public void checkInternet() {

        addListenerHashtags();

    }

    @Override
    public void addListenerHashtags() {

        hashtags = database.getReference("promotion/categoty/");
        hashtags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()
                     ) {

                    CategoryObject object = ds.getValue(CategoryObject.class);
                    if(object != null) {
                        object.setKey(ds.getKey());
                        list.add(object);
                    }

                }

                generateAllHashtags(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void generateAllHashtags(List<CategoryObject> _list) {

        List<CategoryObject> list = new ArrayList<>();
        CategoryObject objectAdd = new CategoryObject();
        objectAdd.setName(context.getResources().getString(R.string.your_hashtags));

        CategoryObject objectFavorite = new CategoryObject();
        objectFavorite.setName(context.getResources().getString(R.string.favotite));

        list.add(objectAdd);
        list.add(objectFavorite);
        list.addAll(_list);
        _view.setRecycler(
                list);

    }



}
