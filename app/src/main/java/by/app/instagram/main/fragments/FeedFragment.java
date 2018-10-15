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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.AudienceAdapter;
import by.app.instagram.adapter.FeedAdapter;
import by.app.instagram.enums.TypeFeed;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.FeedContract;
import by.app.instagram.main.presenters.FeedPresenter;
import by.app.instagram.model.firebase.Image;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.view.filter.FilterView;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;

@SuppressLint("ValidFragment")
public class FeedFragment extends Fragment implements FeedContract.View, View.OnClickListener {

    ImageView img_menu, img_filter;
    TextView tv_fragment_title;
    FilterView filter;

    Context context;
    View v;
    CardView progress;

    CardView card_date, card_likes, card_comments, card_er;
    ImageView img_card_date, img_card_likes, img_card_comments, img_card_er;

    int img_card_date_position, img_card_likes_position,
            img_card_comments_position, img_card_er_position = 0;

    ImageView selected_img;
    MainActivity activity;

    TextView tv_empty_list;
    RecyclerView rec_posts;

    FeedPresenter _presenter;

    public FeedFragment(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_feed, container, false);
        initCards();
        initAppBar();
        initFilter();

        if(_presenter == null ) _presenter = new FeedPresenter(this, v.getContext());
        return v;
    }

    @Override
    public void initCards() {

        tv_empty_list = v.findViewById(R.id.empty_list);
        rec_posts = v.findViewById(R.id.rec_posts);

        card_date = v.findViewById(R.id.card_h1);
        card_likes = v.findViewById(R.id.card_h2);
        card_comments = v.findViewById(R.id.card_h3);
        card_er = v.findViewById(R.id.card_h4);

        img_card_date = v.findViewById(R.id.img_card_h1);
        img_card_likes = v.findViewById(R.id.img_card_h2);
        img_card_comments = v.findViewById(R.id.img_card_h3);
        img_card_er = v.findViewById(R.id.img_card_h4);

        card_date.setOnClickListener(this);
        card_likes.setOnClickListener(this);
        card_comments.setOnClickListener(this);
        card_er.setOnClickListener(this);

        selected_img = img_card_date;
    }

    @Override
    public void initFilter() {
        filter =  v.findViewById(R.id.filter);
        filter.setTypeFilter(TypeFilter.Feed);
        filter.setCountPosts(100);
        filter.setTypeSpinnerFilter(TypeSpinnerFilter.CountPosts);

        filter.card_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.setTypeSpinnerFilter(filter.getTypeSpinnerFilter());

                if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.All){
                    _presenter.sortData();

                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.CurrentMonth){
                    _presenter.sortData();

                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.SelectMonth){

                    _presenter.setPeriod1(filter.getPeriod1());
                    _presenter.setPeriod2(filter.getPeriod2());
                    _presenter.sortData();

                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.CountPosts){
                    _presenter.setCountPosts(filter.getCountPosts());
                    _presenter.sortData();
                }
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
    public void initAppBar() {
        img_menu = v.findViewById(R.id.img_menu);

        img_filter = v.findViewById(R.id.img_right);
        tv_fragment_title = v.findViewById(R.id.title_bar);

        tv_fragment_title.setText(getResources().getString(R.string.fragment_feed));
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showMenu();
                YoYo.with(Techniques.RubberBand).duration(500).playOn(img_menu);
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
    public void setSelectedCard(CardView card) {
        CardView[] arrCards = {card_date, card_likes, card_comments, card_er};

        for (CardView item : arrCards){
            item.setCardBackgroundColor(getResources().getColor(R.color.border_ava));
        }

        card.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));


    }

    @Override
    public void animClickCard(View v) {
        YoYo.with(Techniques.RubberBand).duration(500).playOn(v);
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
    public void animRotate(View view, int type) {
        RotateAnimation rotate = null;
        if(type != 2 && type != 3){
            if(type ==0){
                rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }else rotate = new RotateAnimation(90, 00, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }else {
            if(selected_img == view){
                if(type ==2){
                    rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                }else if(type == 3)rotate = new RotateAnimation(180, 00, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }else {
                selected_img = (ImageView) view;
            }
        }
        if(rotate != null){
            rotate.setDuration(300);
            rotate.setInterpolator(new LinearInterpolator());
            rotate.setFillAfter(true);
            view.startAnimation(rotate);
        }
    }

    @Override
    public void showNoInternetConnection() {
        Toast.makeText(v.getContext(), getResources().getString(R.string.err_connection_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackUpdate() {
        Snackbar snackbar = Snackbar.make(img_filter,
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
    public void setRecycler(List<MediaObject> list) {
        FeedAdapter audienceAdapter = new FeedAdapter(list, v.getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_posts.setLayoutManager(mLayoutManager);
        rec_posts.setAdapter(audienceAdapter);
    }

    @Override
    public int getDateCardPosition() {
        return this.img_card_date_position;
    }

    @Override
    public int getLieksCardPosition() {
        return this.img_card_likes_position;
    }

    @Override
    public int getCommentsCardPosition() {
        return this.img_card_comments_position;
    }

    @Override
    public int getERCardPosition() {
        return this.img_card_er_position;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        animClickCard(view);
        switch (id){
            case R.id.card_h1:
                _presenter.setTypeFeed(TypeFeed.Date);
                setSelectedCard(card_date);
                if(selected_img == img_card_date){
                    if(img_card_date_position == 0) {
                        animRotate(img_card_date, 2);
                        if(selected_img == img_card_date) img_card_date_position = 180;
                    }
                    else {
                        animRotate(img_card_date, 3);
                        if(selected_img == img_card_date) img_card_date_position = 0;
                    }
                }selected_img = img_card_date;
                _presenter.sortData();
                break;
            case R.id.card_h2:
                _presenter.setTypeFeed(TypeFeed.Likes);
                setSelectedCard(card_likes);
                if(selected_img == img_card_likes){
                    if(img_card_likes_position == 0) {
                        animRotate(img_card_likes, 2);
                        if(selected_img == img_card_likes)img_card_likes_position = 180;
                    }
                    else {
                        if(selected_img == img_card_likes) animRotate(img_card_likes, 3);
                        img_card_likes_position = 0;
                    }
                }else selected_img = img_card_likes;
                _presenter.sortData();
                break;
            case R.id.card_h3:
                _presenter.setTypeFeed(TypeFeed.Comments);
                setSelectedCard(card_comments);
                if(selected_img == img_card_comments){if(img_card_comments_position == 0) {
                    animRotate(img_card_comments, 2);
                    if(selected_img == img_card_comments) img_card_comments_position = 180;
                }
                else {
                    animRotate(img_card_comments, 3);
                    if(selected_img == img_card_comments) img_card_comments_position = 0;
                }

                }else selected_img = img_card_comments;
                _presenter.sortData();
                break;
            case R.id.card_h4:
                _presenter.setTypeFeed(TypeFeed.ER);
                setSelectedCard(card_er);
                if(selected_img == img_card_er){
                    if(img_card_er_position == 0) {
                        animRotate(img_card_er, 2);
                        if(selected_img == img_card_er) img_card_er_position = 180;
                    }
                    else {
                        animRotate(img_card_er, 3);
                        if(selected_img == img_card_er) img_card_er_position = 0;
                    }
                }else selected_img = img_card_er;
                _presenter.sortData();
                break;
        }

    }

    @Override
    public void onPause() {
        try {
            _presenter.destroyListeners();
        }catch (Exception e){

        }
        super.onPause();
    }

    @Override
    public void onResume() {
        _presenter.checkInernet();
        super.onResume();
    }
}
