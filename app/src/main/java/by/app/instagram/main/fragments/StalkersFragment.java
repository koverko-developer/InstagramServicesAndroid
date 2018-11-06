package by.app.instagram.main.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.StalkersAdapter;
import by.app.instagram.adapter.UserHashTagsAdapter;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.StalkersContract;
import by.app.instagram.main.presenters.StalkersPresenter;
import by.app.instagram.model.firebase.StalkersObject;
import by.app.instagram.view.filter.FilterView;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;

@SuppressLint("ValidFragment")
public class StalkersFragment extends Fragment implements StalkersContract.View{

    View v;
    Context context;
    StalkersPresenter _presenter;

    CardView progress;

    ImageView img_menu, img_filter, img;
    TextView tv_fragment_title, tv_empty_list, txt;

    RecyclerView rec_users;

    FilterView filter;
    MainActivity activity;
    private AdView mAdView;

    public StalkersFragment(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_stalkers, container, false);

        initCardH();
        initAppBar();
        initFilter();
        initAds();

        if(_presenter == null) _presenter = new StalkersPresenter(this, v.getContext());

        return v;
    }

    @Override
    public void initCardH() {

        img = (ImageView) v.findViewById(R.id.img_1);
        tv_empty_list = v.findViewById(R.id.empty_list);
        rec_users = v.findViewById(R.id.rec_users);

    }

    @Override
    public void initAppBar() {
        img_menu = v.findViewById(R.id.img_menu);
        img_filter = v.findViewById(R.id.img_right);
        tv_fragment_title = v.findViewById(R.id.title_bar);

        tv_fragment_title.setText(getResources().getString(R.string.fragment_stalkers));
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
    public void initFilter() {
        filter =  v.findViewById(R.id.filter);
        filter.setTypeFilter(TypeFilter.Auditory);

        filter.card_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                _presenter.setTypeSpinnerFilter(filter.getTypeSpinnerFilter());
//
//                if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.SelectMonth){
//
//                    _presenter.setPeriod1(filter.getPeriod1());
//                    _presenter.setPeriod2(filter.getPeriod2());
//
//                }
//                _presenter.sortAudience();
                hideFilter();

            }
        });

        filter.card_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilter();
            }
        });
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
    public void hideFilter() {
        animRotate(img_filter, 1);
        YoYo.with(Techniques.SlideOutUp).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                filter.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).duration(300).playOn(filter);
    }

    @Override
    public void showFilter() {
        animRotate(img_filter, 0);
        YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                filter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).duration(300).playOn(filter);
    }

    @Override
    public void showNoInternetConnection() {

        Toast.makeText(v.getContext(), getResources().getString(R.string.err_connection_internet),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void animRotate(View view, int type) {

        RotateAnimation rotate;
        if(type ==0){
            rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }else rotate = new RotateAnimation(90, 00, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    @Override
    public void setRecycler(List<StalkersObject> _list) {

        if(_list.size() == 0) {
            tv_empty_list.setVisibility(View.VISIBLE);
            rec_users.setVisibility(View.GONE);
        }else {
            tv_empty_list.setVisibility(View.GONE);
            rec_users.setVisibility(View.VISIBLE);
        }

        StalkersAdapter adapter = new StalkersAdapter(_list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_users.setLayoutManager(mLayoutManager);
        rec_users.setAdapter(adapter);
    }

    @Override
    public void showSnackUpdate() {
        try {
            Snackbar snackbar = Snackbar.make(v.findViewById(R.id.img_1),
                    getResources().getString(R.string.update_data), 6000);
            snackbar.setAction(getResources().getString(R.string.yes), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _presenter.getAllData();
                }
            });

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initAds() {
        MobileAds.initialize(v.getContext(), getResources().getString(R.string.ad_id1));
        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        try{
            _presenter.destroyListenersAll();
            _presenter.destroyProgressListener();

        }catch (Exception e){}
        super.onPause();
    }
}
