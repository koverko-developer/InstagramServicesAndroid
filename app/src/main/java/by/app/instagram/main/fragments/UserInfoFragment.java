package by.app.instagram.main.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.adapter.TopCommentsUsersAdapter;
import by.app.instagram.adapter.TopLikerUsersAdapter;
import by.app.instagram.db.MyTexyUtil;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.UserInfoContract;
import by.app.instagram.main.presenters.UserInfoPresenter;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.fui.UserInfoTop;
import by.app.instagram.model.vk.VKUserInfo;

@SuppressLint("ValidFragment")
public class UserInfoFragment extends Fragment implements UserInfoContract.ViewModel{

    Context context;
    View v;

    TextView tv_followed_by, tv_follows, tv_count_photo, tv_count_slider, tv_count_video,
             tv_count_likes, tv_count_comments, tv_count_views;
    ImageView img_ava, img_menu, img_math, img_share, img;
    CardView card_h, card_2, card_3, card_4, card_5;
    RecyclerView rec_top_likers, rec_top_comments;
    CardView progress;
    RelativeLayout rrel;
    UserInfoPresenter _presenter;
    MainActivity activity;

    private AdView mAdView;

    private LinearLayoutManager mLayoutManager;

    public UserInfoFragment(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.info_fragment, container, false);
        initCardViewUI();
        initCardViewMediaUI();
        initCardViewMedia2UI();
        initCardViewMedia3UI();
        initCardViewMedia4UI();
        initAds();
        if(_presenter == null) _presenter = new UserInfoPresenter(v.getContext(), this);

        return v;
    }

    @Override
    public void onResume() {
        _presenter.addListenerTopComments();
        _presenter.addListenerInfo();
        _presenter.addListenerTopComments();
        _presenter.addListenerProgress();
        super.onResume();
    }

    @Override
    public void onStart() {
        _presenter.initRealm();
        super.onStart();
    }

    @Override
    public void initCardViewUI() {

        //img = (ImageView) v.findViewById(R.id.img_1);
        img_share = (ImageView) v.findViewById(R.id.img_right);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.text_share));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        img_math = (ImageView) v.findViewById(R.id.img_math);
        img_menu = v.findViewById(R.id.img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showMenu();
                YoYo.with(Techniques.RubberBand).duration(500).playOn(img_menu);
            }
        });
        rrel = v.findViewById(R.id.rrel);
        card_h = v.findViewById(R.id.card);
        tv_followed_by = v.findViewById(R.id.followed_by);
        tv_follows = v.findViewById(R.id.follows);
        img_ava = v.findViewById(R.id.ava);

        cardAnimation(card_h, Techniques.BounceInDown);
    }

    @Override
    public void initCardViewMediaUI() {

        card_2 = v.findViewById(R.id.card_h2);
        tv_count_photo = v.findViewById(R.id.count_photo);
        tv_count_video = v.findViewById(R.id.count_video);
        tv_count_slider = v.findViewById(R.id.count_slider);

        cardAnimation(card_2, Techniques.SlideInLeft);
    }

    @Override
    public void initCardViewMedia2UI() {
        card_3 = v.findViewById(R.id.card_h3);
        tv_count_likes = v.findViewById(R.id.count_likes);
        tv_count_comments = v.findViewById(R.id.count_comments);
        tv_count_views = v.findViewById(R.id.count_views);
        cardAnimation(card_3, Techniques.SlideInRight);
    }

    @Override
    public void initCardViewMedia3UI() {
        card_4 = v.findViewById(R.id.card_h4);
        rec_top_likers = v.findViewById(R.id.rec_top_likers);
        cardAnimation(card_4, Techniques.BounceInUp);
    }

    @Override
    public void initCardViewMedia4UI() {
        card_5 = v.findViewById(R.id.card_h4);
        rec_top_comments = v.findViewById(R.id.rec_top_comments);
        card_5.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCardViewUI(VKUserInfo viewUI) {
        String p_url = viewUI.getmData().getProfilePicture();
        if(p_url != null){

            Glide.with(v.getContext())
                    .load(p_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(img_ava);
        }

        long followed_by = viewUI.getmData().getmCounts().getFollowedBy();
        if(followed_by != 0) {
            tv_followed_by.setText(MyTexyUtil.countsLong(followed_by));
        }

        long follows = viewUI.getmData().getmCounts().getFollows();
        if(follows != 0) {
            tv_follows.setText(MyTexyUtil.countsLong(follows));
        }

        textAnimation(tv_follows);
        textAnimation(tv_followed_by);
    }

    @Override
    public void addCardViewMediaInfo(UserInfoMedia media) {

        tv_count_photo.setText(MyTexyUtil.countsInt(media.getCount_photo()));
        tv_count_slider.setText(MyTexyUtil.countsInt(media.getCount_corousel()));
        tv_count_video.setText(MyTexyUtil.countsInt(media.getCount_video()));

        textAnimation(tv_count_photo);
        textAnimation(tv_count_slider);
        textAnimation(tv_count_video);
    }

    @Override
    public void addCardViewMedia2Info(UserInfoMedia media) {

        tv_count_likes.setText(MyTexyUtil.countsInt(media.getCount_like()));
        tv_count_comments.setText(MyTexyUtil.countsInt(media.getCount_comments()));
        tv_count_views.setText(MyTexyUtil.countsInt(media.getCount_view()));
    }

    @Override
    public void addCardViewMedia3Info(List<UserInfoTop> list) {

        TopLikerUsersAdapter adapter = new TopLikerUsersAdapter(list, v.getContext());
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_top_likers.setLayoutManager(mLayoutManager);
        rec_top_likers.setAdapter(adapter);

    }

    @Override
    public void addCardViewMedia4Info(List<UserInfoTop> list) {

        TopCommentsUsersAdapter adapter = new TopCommentsUsersAdapter(list, v.getContext());
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_top_comments.setLayoutManager(mLayoutManager);
        rec_top_comments.setAdapter(adapter);

    }

    @Override
    public void cardAnimation(View view, Techniques techniques) {

        if(view.getVisibility() == View.GONE){
            YoYo.with(techniques).duration(1000).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    view.setVisibility(View.VISIBLE);
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
            }).playOn(view);
        }

    }

    @Override
    public void textAnimation(View v) {
        YoYo.with(Techniques.Landing).duration(2000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

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
        }).playOn(v);
    }

    @Override
    public void hideProgress() {

        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoInternetConnection() {
        Toast.makeText(v.getContext(), getResources().getString(R.string.err_connection_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackUpdate() {
        Snackbar snackbar = Snackbar.make(img_math,
                getResources().getString(R.string.update_data), 6000);
        snackbar.setAction(getResources().getString(R.string.yes), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.getUI();
            }
        });
        snackbar.show();
    }

    @Override
    public void initAds() {
        try {
            MobileAds.initialize(v.getContext(), getResources().getString(R.string.ad_id2));
            mAdView = (AdView) v.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCardUI(String _img, Long _followers, Long _following) {
        String p_url = _img;
        if(p_url != null){

            Glide.with(v.getContext())
                    .load(p_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(img_ava);
        }

        long followed_by = _followers;
        if(followed_by != 0) {
            tv_followed_by.setText(MyTexyUtil.countsLong(followed_by));
        }

        long follows = _following;
        if(follows != 0) {
            tv_follows.setText(MyTexyUtil.countsLong(follows));
        }

        textAnimation(tv_follows);
        textAnimation(tv_followed_by);
    }

    @Override
    public void onStop() {
        super.onStop();
        _presenter.destroyListener();
        _presenter.closeRealm();
    }
}
