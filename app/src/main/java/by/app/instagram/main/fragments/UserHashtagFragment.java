package by.app.instagram.main.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.UserHashTagsAdapter;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.UserHashtagContract;
import by.app.instagram.main.presenters.HashtagsUserPresenter;
import by.app.instagram.model.firebase.HashTagObject;
import by.app.instagram.view.filter.FilterView;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;

@SuppressLint("ValidFragment")
public class UserHashtagFragment extends Fragment implements UserHashtagContract.View{

    private static String TAG = UserHashtagFragment.class.getName();

    ImageView img_menu, img_filter, img;
    TextView tv_fragment_title;
    CardView progress;

    FilterView filter;

    Context contex;
    View v;
    UserHashtagContract.Presenter _presenter;

    SearchView searchView;
    TextView tv_empty_hashtags;
    LinearLayout lin_all_data;
    RecyclerView rec_popular, rec_count_likes, rec_count_comments;

    private AdView mAdView;
    MainActivity activity;

    public UserHashtagFragment(Context contex) {
        this.contex = contex;
        activity = (MainActivity) contex;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hashtags_user, container, false);

        initAppBar();
        initLists();

        if(_presenter == null) _presenter = new HashtagsUserPresenter(v.getContext(), this);

        return v;
    }

    @Override
    public void onPause() {
        try{

            _presenter.destroyListenersAll();
            _presenter.destroyProgressListener();

        }catch (Exception e){}
        super.onPause();
    }

    @Override
    public void initAppBar() {

        img = (ImageView) v.findViewById(R.id.img_1);
        img_menu = v.findViewById(R.id.img_menu);
        img_filter = v.findViewById(R.id.img_right);
        tv_fragment_title = v.findViewById(R.id.title_bar);

        tv_fragment_title.setText(getResources().getString(R.string.fragment_hashtags));
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showMenu();
            }
        });

        img_filter.setImageDrawable(null);
        img_filter.setEnabled(false);

    }

    @Override
    public void initLists() {

       searchView = v.findViewById(R.id.search_hashtags);

       tv_empty_hashtags = v.findViewById(R.id.empty_list);
       lin_all_data = v.findViewById(R.id.lin_all_data);
       rec_popular = v.findViewById(R.id.rec_popular);
       rec_count_likes = v.findViewById(R.id.rec_count_likes);
       rec_count_comments = v.findViewById(R.id.rec_count_comments);

    }

    @Override
    public void showProgress() {
        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
    }
    @Override
    public void showNoInternetConnection() {

        Toast.makeText(v.getContext(), getResources().getString(R.string.err_connection_internet),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showSnackUpdate() {
        Snackbar snackbar = Snackbar.make(img,
                getResources().getString(R.string.update_data), 6000);
        snackbar.setAction(getResources().getString(R.string.yes), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.getAllData();
            }
        });
        snackbar.show();
    }

    @Override
    public void setRecyclerAll(List<HashTagObject> list) {

        if(list.size() == 0) {
            tv_empty_hashtags.setVisibility(View.VISIBLE);
            lin_all_data.setVisibility(View.GONE);
        }else {
            tv_empty_hashtags.setVisibility(View.GONE);
            lin_all_data.setVisibility(View.VISIBLE);
        }

        UserHashTagsAdapter adapter = new UserHashTagsAdapter(list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_popular.setLayoutManager(mLayoutManager);
        rec_popular.setAdapter(adapter);

    }

    @Override
    public void setRecyclerLikes(List<HashTagObject> list) {
        UserHashTagsAdapter adapter = new UserHashTagsAdapter(list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_count_likes.setLayoutManager(mLayoutManager);
        rec_count_likes.setAdapter(adapter);
    }

    @Override
    public void setRecyclerComments(List<HashTagObject> list) {
        UserHashTagsAdapter adapter = new UserHashTagsAdapter(list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_count_comments.setLayoutManager(mLayoutManager);
        rec_count_comments.setAdapter(adapter);
    }

    @Override
    public void initAds() {
        MobileAds.initialize(getContext(), getResources().getString(R.string.ad_id1));
        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

}
