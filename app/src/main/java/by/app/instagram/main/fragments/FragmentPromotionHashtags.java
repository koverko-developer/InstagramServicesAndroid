package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.CategoryHashtagsAdapter;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.PromotionHashtagsContract;
import by.app.instagram.main.presenters.PromotionHashtagsPresenter;
import by.app.instagram.model.firebase.CategoryObject;

@SuppressLint("ValidFragment")
public class FragmentPromotionHashtags extends Fragment
                    implements PromotionHashtagsContract.View{

    View v;
    Context context;

    CardView progress;
    ImageView img_menu, img_filter;
    TextView tv_fragment_title;
    RecyclerView recyclerView;
    MainActivity activity;

    PromotionHashtagsPresenter _presenter;

    public FragmentPromotionHashtags(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_promotion_hashtags, container, false);

        if(_presenter == null) _presenter = new PromotionHashtagsPresenter(this, v.getContext());

        initAppBar();
        initView();

        return v;
    }


    @Override
    public void initView() {
        recyclerView = (RecyclerView) v.findViewById(R.id.h_recycler);
    }

    @Override
    public void initAppBar() {
        img_menu = v.findViewById(R.id.img_menu);
        img_filter = v.findViewById(R.id.img_right);
        tv_fragment_title = v.findViewById(R.id.title_bar);

        tv_fragment_title.setText(getResources().getString(R.string.posts_fragment));
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showMenu();
                YoYo.with(Techniques.RubberBand).duration(500).playOn(img_menu);
            }
        });

        img_filter.setImageDrawable(null);
        img_filter.setEnabled(false);

    }


    @Override
    public void showNoInternetConnection() {

    }

    @Override
    public void setRecycler(List<CategoryObject> _list) {

        recyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 3));
        CategoryHashtagsAdapter adapter = new CategoryHashtagsAdapter(_list, v.getContext());
        recyclerView.setAdapter(adapter);

    }
}
