package by.app.instagram.main.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.AudienceAdapter;
import by.app.instagram.db.AudienceDatesSort;
import by.app.instagram.enums.TypeAudience;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.AudienceContract;
import by.app.instagram.main.presenters.AudiencePresenter;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.view.filter.FilterView;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;
import im.dacer.androidcharts.LineView;

@SuppressLint("ValidFragment")
public class AudienceFragment extends Fragment implements AudienceContract.View,
        View.OnClickListener {

    private static String TAG = AudienceFragment.class.getName();

    CardView card_h1, card_h2, card_graph_g, card_list_g;
    public TextView tv_count_follows, tv_count_unfollows, tv_empty_list;
    TextView tv_graph, audience_tv_card_people;
    LineView chartFollows;
    RecyclerView recycler_people;

    Context context;
    View v;

    CardView progress;

    ImageView img_menu, img_filter, img;
    TextView tv_fragment_title;

    FilterView filter;

    AudiencePresenter _presenter;
    MainActivity activity;

    private AdView mAdView;

    public AudienceFragment(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.audience_fragment, container, false);
        initCardH();
        initAppBar();
        initFilter();
        initCardList();
        initGraph();
        initAds();

        if(_presenter == null) _presenter = new AudiencePresenter(this, v.getContext());

        return v;
    }

    @Override
    public void initCardH() {

        img = (ImageView) v.findViewById(R.id.img_1);
        tv_empty_list = v.findViewById(R.id.tv_empty_list);
        card_graph_g = v.findViewById(R.id.card_graph_g);
        card_list_g = v.findViewById(R.id.card_list_g);

        tv_count_follows = v.findViewById(R.id.tv_count_follows);
        tv_count_unfollows = v.findViewById(R.id.tv_count_unfollows);
        card_h1 = v.findViewById(R.id.card_follows);
        card_h2 = v.findViewById(R.id.card_unfollows);

        card_h1.setOnClickListener(this);
        card_h2.setOnClickListener(this);

    }

    @Override
    public void initAppBar() {

        img_menu = v.findViewById(R.id.img_menu);
        img_filter = v.findViewById(R.id.img_right);
        tv_fragment_title = v.findViewById(R.id.title_bar);

        tv_fragment_title.setText(getResources().getString(R.string.audience_fragment));
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showMenu();
            }
        });

        img_filter.setImageDrawable(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter.getVisibility() == View.VISIBLE) hideFilter();
                else showFilter();

            }
        });

    }

    @Override
    public void initFilter() {
        filter =  v.findViewById(R.id.filter);
        filter.setTypeFilter(TypeFilter.Auditory);

        filter.card_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.setTypeSpinnerFilter(filter.getTypeSpinnerFilter());

                if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.SelectMonth){

                    _presenter.setPeriod1(filter.getPeriod1());
                    _presenter.setPeriod2(filter.getPeriod2());

                }
                _presenter.sortAudience();
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
    public void initGraph() {

        tv_graph = v.findViewById(R.id.audience_tv_card);
        try {
            chartFollows = v.findViewById(R.id.line_view_follows);
            chartFollows.setColorArray(new int[]{
                    Color.parseColor("#0097A9"), Color.parseColor("#9C27B0"),
                    Color.parseColor("#2196F3"), Color.parseColor("#009688")
            });
            chartFollows.setDrawDotLine(true);
            chartFollows.setShowPopup(LineView.SHOW_POPUPS_All);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initCardList() {

        audience_tv_card_people = v.findViewById(R.id.audience_tv_card_people);
        recycler_people = v.findViewById(R.id.audience_recycler_people);

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
    public void onPause() {
        _presenter.destroyListenerProgress();
        _presenter.destroyListenerFirebase();
        super.onPause();
    }

    @Override
    public void animClickCard(View v) {

        YoYo.with(Techniques.RubberBand).duration(500).playOn(v);

    }

    @Override
    public void changeCard(CardView v) {

        CardView[] arrCard = new CardView[]{card_h1, card_h2};

        for (CardView card: arrCard
             ) {
            card.setCardBackgroundColor(getResources().getColor(R.color.border_ava));
        }

        v.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void setUnfollow(int count, List<AudienceObject> unfollow_list) {

        tv_graph.setText(getResources().getString(R.string.graph_unfollows));
        audience_tv_card_people.setText(getResources().getString(R.string.who_unfollows));
        tv_count_unfollows.setText(String.valueOf(count));
        animText(tv_count_unfollows);
        setRecycler(unfollow_list);
        addToValueToLineView(AudienceDatesSort.getAudience(unfollow_list));

    }

    @Override
    public void setfollow(int count, List<AudienceObject> follow_list) {

        tv_graph.setText(getResources().getString(R.string.graph_follows));
        audience_tv_card_people.setText(getResources().getString(R.string.who_follows));
        tv_count_follows.setText(String.valueOf(count));
        animText(tv_count_follows);
        setRecycler(follow_list);
        addToValueToLineView(AudienceDatesSort.getAudience(follow_list));

    }

    @Override
    public void setRecycler(List<AudienceObject> list) {

        AudienceAdapter audienceAdapter = new AudienceAdapter(list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_people.setLayoutManager(mLayoutManager);
        recycler_people.setAdapter(audienceAdapter);

    }

    @Override
    public void animText(View v) {
        YoYo.with(Techniques.Landing).duration(1000).playOn(v);
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
    public void addToValueToLineView(List<ChartItem> list) {

        if(list != null && list.size() != 0){
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<Integer> listFollow = new ArrayList<>();

            for (ChartItem item:list
                    ) {
                dates.add(item.getDates());
                listFollow.add(item.getCount());
            }

            ArrayList<ArrayList<Integer>> arrFollow = new ArrayList<>();

            arrFollow.add(listFollow);

            chartFollows.setBottomTextList(dates);

            chartFollows.setDataList(arrFollow);

            card_list_g.setVisibility(View.VISIBLE);
            card_graph_g.setVisibility(View.VISIBLE);
            tv_empty_list.setVisibility(View.GONE);

        }else {
            card_list_g.setVisibility(View.GONE);
            card_graph_g.setVisibility(View.GONE);
            tv_empty_list.setVisibility(View.VISIBLE);
            //Toast.makeText(context, getResources().getString(R.string.empty_list),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void setUnfollowsTV(int count) {
        tv_count_unfollows.setText(String.valueOf(count));
        animText(tv_count_unfollows);
    }

    @Override
    public void setfollowsTV(int count) {
        tv_count_follows.setText(String.valueOf(count));
        animText(tv_count_follows);
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
    public void initAds() {
        MobileAds.initialize(v.getContext(), getResources().getString(R.string.ad_id1));
        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.card_follows:
                animClickCard(view);
                _presenter.clickCard(TypeAudience.Follow);
                changeCard(card_h1);
                break;
            case R.id.card_unfollows:
                animClickCard(view);
                _presenter.clickCard(TypeAudience.UnFollow);
                changeCard(card_h2);
                break;
        }

    }
}
