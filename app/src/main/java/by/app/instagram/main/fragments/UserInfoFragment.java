package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import by.app.instagram.R;
import by.app.instagram.main.contracts.UserInfoContract;
import by.app.instagram.main.presenters.UserInfoPresenter;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.vk.VKUserInfo;

@SuppressLint("ValidFragment")
public class UserInfoFragment extends Fragment implements UserInfoContract.ViewModel{

    Context context;
    View v;

    TextView tv_followed_by, tv_follows, tv_count_photo, tv_count_slider, tv_count_video,
             tv_count_likes, tv_count_comments, tv_count_views;
    ImageView img_ava;
    CardView card_h, card_2, card_3;
    UserInfoPresenter _presenter;

    public UserInfoFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.info_fragment, container, false);
        initCardViewUI();
        initCardViewMediaUI();
        initCardViewMedia2UI();
        if(_presenter == null) _presenter = new UserInfoPresenter(v.getContext(), this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initCardViewUI() {

        card_h = v.findViewById(R.id.card);
        tv_followed_by = v.findViewById(R.id.followed_by);
        tv_follows = v.findViewById(R.id.follows);
        img_ava = v.findViewById(R.id.ava);

    }

    @Override
    public void initCardViewMediaUI() {

        card_2 = v.findViewById(R.id.card_h2);
        tv_count_photo = v.findViewById(R.id.count_photo);
        tv_count_video = v.findViewById(R.id.count_video);
        tv_count_slider = v.findViewById(R.id.count_slider);

    }

    @Override
    public void initCardViewMedia2UI() {
        card_3 = v.findViewById(R.id.card_h3);
        tv_count_likes = v.findViewById(R.id.count_likes);
        tv_count_comments = v.findViewById(R.id.count_comments);
        tv_count_views = v.findViewById(R.id.count_views);
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
            tv_followed_by.setText(String.valueOf(followed_by));
        }

        long follows = viewUI.getmData().getmCounts().getFollows();
        if(follows != 0) {
            tv_follows.setText(String.valueOf(follows));
        }
    }

    @Override
    public void addCardViewMediaInfo(UserInfoMedia media) {

        tv_count_photo.setText(String.valueOf(media.getCount_photo()));
        tv_count_slider.setText(String.valueOf(media.getCount_corousel()));
        tv_count_video.setText(String.valueOf(media.getCount_video()));

    }

    @Override
    public void addCardViewMedia2Info(UserInfoMedia media) {
        tv_count_likes.setText(String.valueOf(media.getCount_like()));
        tv_count_comments.setText(String.valueOf(media.getCount_comments()));
        tv_count_views.setText(String.valueOf(media.getCount_view()));
    }
}
