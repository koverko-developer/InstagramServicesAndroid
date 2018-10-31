package by.app.instagram.main.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.R;
import by.app.instagram.enums.TypePosts;
import by.app.instagram.main.MainActivity;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.main.presenters.PostsPresenter;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.view.filter.FilterView;
import by.app.instagram.view.filter.IFilterView;
import by.app.instagram.view.filter.TypeFilter;
import by.app.instagram.view.filter.TypeSpinnerFilter;
import im.dacer.androidcharts.LineView;

@SuppressLint("ValidFragment")
public class FragmentPosts extends Fragment implements PostsContract.ViewModel,
        View.OnClickListener, IFilterView.Click{

    private static String TAG = FragmentPosts.class.getName();

    ImageView img_menu, img_filter;
    TextView tv_fragment_title;

    TypePosts typePosts = TypePosts.All;

    View v;
    Context context;
    PostsPresenter _presenter;

    CardView card_3, card_media_all, card_media_photo, card_media_video, card_media_slider;
    TextView tv_count_likes, tv_count_comments, tv_count_views, tv_count_all,
             tv_count_photo, tv_count_video, tv_count_slider;

    LineChart mChart;
    CardView progress;

    LineView chartLikes, chartComments, chartViews;
    FilterView filter;

    MainActivity activity;

    private AdView mAdView;

    public FragmentPosts(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.posts_fragment, container, false);
        //initChart();
        initCardH();
        initCardTypeMedia();
        initChartComments();
        initChartLikes();
        initChartViews();
        initFilter();
        initAppBar();

        if(_presenter == null) _presenter = new PostsPresenter(this, v.getContext());

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initChart() {
//        ArrayList<Entry> yVals = new ArrayList<>();
//
//        for (int i = 0; i< 1; i++){
////            float val = (float) (Math.random() * 5) + 20;
//            yVals.add(new Entry(i, 0));
//        }
//
//        mChart = v.findViewById(R.id.chartLikes);
//
//
//        mChart.setBackgroundColor(Color.WHITE);
//        mChart.setDrawGridBackground(false);
//        mChart.setDrawBorders(false);
//        //mChart.zoomToCenter(10f, 0f);
//        mChart.getAxisRight().setEnabled(false);
//        YAxis y = mChart.getAxisLeft();
//        y.setDrawGridLines(false);
//        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        mChart.getLegend().setEnabled(false);
//
//        mChart.setViewPortOffsets(40f, 32f, 20f, 0);
//        mChart.getDescription().setEnabled(false);
//
//        XAxis x = mChart.getXAxis();
//        x.setDrawGridLines(false);
//
//        //x.setEnabled(true);
//        x.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
//        x.setTextSize(10f);
//        x.setLabelRotationAngle(-45f);
//
//
//        LineDataSet set1;
//        set1 = new LineDataSet(yVals, "DataSet 1");
//        set1.setDrawValues(true);
//        set1.setDrawFilled(true);
//
//        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set1.setCubicIntensity(0.2f);
//        set1.setDrawCircles(true);
//        set1.setLineWidth(1.8f);
//        set1.setCircleRadius(4f);
//        set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(getResources().getColor(R.color.colorPrimary));
//        set1.setFillColor(getResources().getColor(R.color.colorPrimary));
//        set1.setFillAlpha(100);
//        set1.setDrawHighlightIndicators(false);
//        set1.setFillFormatter(new IFillFormatter() {
//            @Override
//            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                return -10;
//            }
//        });
//
//        LineData data = new LineData(set1);
//        mChart.setData(data);
//        try {
//            mChart.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void initCardH() {

        card_media_all = v.findViewById(R.id.card_all);
        card_media_photo = v.findViewById(R.id.card_photo);
        card_media_video = v.findViewById(R.id.card_video);
        card_media_slider = v.findViewById(R.id.card_slider);

        tv_count_all = v.findViewById(R.id.tv_count_all);
        tv_count_photo = v.findViewById(R.id.tv_count_photo);
        tv_count_video = v.findViewById(R.id.tv_count_video);
        tv_count_slider = v.findViewById(R.id.tv_count_slider);

        cardAnimation(card_media_all);
        cardAnimation(card_media_video);
        cardAnimation(card_media_photo);
        cardAnimation(card_media_slider);

        card_media_all.setOnClickListener(this);
        card_media_photo.setOnClickListener(this);
        card_media_video.setOnClickListener(this);
        card_media_slider.setOnClickListener(this);
    }

    @Override
    public void initCardTypeMedia() {

        card_3 = v.findViewById(R.id.card_h3);
        card_3.setVisibility(View.VISIBLE);
        tv_count_likes = v.findViewById(R.id.count_likes);
        tv_count_comments = v.findViewById(R.id.count_comments);
        tv_count_views = v.findViewById(R.id.count_views);

        cardAnimation(card_3);
    }

    @Override
    public void initChartLikes() {

        try {
            chartLikes = v.findViewById(R.id.line_view_likes);
            chartLikes.setColorArray(new int[]{
                    Color.parseColor("#0097A9"), Color.parseColor("#9C27B0"),
                    Color.parseColor("#2196F3"), Color.parseColor("#009688")
            });
            chartLikes.setDrawDotLine(true);
            chartLikes.setShowPopup(LineView.SHOW_POPUPS_All);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initChartComments() {

        chartComments = v.findViewById(R.id.line_view_comments);
        chartComments.setColorArray(new int[]{
                Color.parseColor("#0097A9"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688")
        });
        chartComments.setDrawDotLine(true);
        chartComments.setShowPopup(LineView.SHOW_POPUPS_All);

    }

    @Override
    public void initChartViews() {

        chartViews = v.findViewById(R.id.line_view_views);
        chartViews.setColorArray(new int[]{
                Color.parseColor("#0097A9"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688")
        });
        chartViews.setDrawDotLine(true);
        chartViews.setShowPopup(LineView.SHOW_POPUPS_All);

    }

    @Override
    public void initFilter() {
        filter =  v.findViewById(R.id.filter);
        filter.setTypeFilter(TypeFilter.Posts);

        filter.card_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.setTypeSpinnerFilter(filter.getTypeSpinnerFilter());

                if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.All){
                    _presenter.sortTypeMedia(typePosts);


                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.CurrentMonth){

                    _presenter.sortTypeMedia(typePosts);

                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.SelectMonth){

                    _presenter.setPeriod1(filter.getPeriod1());
                    _presenter.setPeriod2(filter.getPeriod2());
                    _presenter.sortTypeMedia(typePosts);

                }else if(filter.getTypeSpinnerFilter() == TypeSpinnerFilter.CountPosts){
                    _presenter.setCountsPosts(filter.getCountPosts());
                    _presenter.sortTypeMedia(typePosts);
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

        tv_fragment_title.setText(getResources().getString(R.string.posts_fragment));
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
    public void cardAnimation(View v) {

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(v);

    }

    @Override
    public void setValueToCardH(UserInfoMedia media) {

        tv_count_comments.setText(String.valueOf(media.getCount_comments()));
        tv_count_likes.setText(String.valueOf(media.getCount_like()));
        tv_count_views.setText(String.valueOf(media.getCount_view()));
        tv_count_photo.setText(String.valueOf(media.getCount_photo()));
        tv_count_video.setText(String.valueOf(media.getCount_video()));
        tv_count_slider.setText(String.valueOf(media.getCount_corousel()));
        tv_count_all.setText(String.valueOf(media.getCount_corousel() +
                     media.getCount_video() + media.getCount_photo()));

        animText(tv_count_comments);
        animText(tv_count_likes);
        animText(tv_count_views);
        animText(tv_count_photo);
        animText(tv_count_video);
        animText(tv_count_slider);
        animText(tv_count_all);

    }

    @Override
    public void setInfoSortTypePost(UserInfoMedia media) {

        tv_count_views.setText(String.valueOf(media.getCount_view()));
        tv_count_likes.setText(String.valueOf(media.getCount_like()));
        tv_count_comments.setText(String.valueOf(media.getCount_comments()));

        animText(tv_count_views);
        animText(tv_count_likes);
        animText(tv_count_comments);
    }

    @Override
    public void setSelectedCard(CardView card) {
        CardView[] arrCards = {card_media_all, card_media_photo, card_media_video, card_media_slider};

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
    public void animText(View v) {
        YoYo.with(Techniques.Landing).duration(1000).playOn(v);
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
    public void addDataToChartLikes(List<ChartItem> list) {

//        ArrayList<Entry> yVals = new ArrayList<>();
//
//        for (int i = 0; i< list.size() ; i++){
////            float val = (float) (Math.random() * 5) + 20;
//            yVals.add(new Entry(i, list.get(i).getCountsLikes()));
//        }
//        ArrayList<String> arr = new ArrayList<>();
//        arr.add("");
//        for (ChartItem d:list
//             ) {
//            d.getDates();
//        }
//        LineDataSet set1;
//        //set1 = new LineDataSet(yVals, "DataSet 1");
//        set1 = getDataSet(yVals);
//        LineData data = new LineData(set1);
//        mChart.setData(data);
//        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                int index = (int) value;
//
//                Log.e(TAG, String.valueOf(index) + " -- " +String.valueOf(value));
////                if(value == 0.0) index = 1;
////                if(value == 0.9) index = 1;
////                else if(index == 0) return "";
////                else if(index == yVals.size()) return "";
////                else if(index == yVals.size() + 1) return "";
//                return list.get(index).getDates();
//            }
//        });
//        XAxis x = mChart.getXAxis();
//        x.setLabelCount(5, true);
//        x.setSpaceMin(50f);
//        x.setSpaceMax(50f);
////        mChart.zoomToCenter(8f, 0f);
////        if(list.size() > 0 &&list.size() < 30) mChart.zoom(3, 1, 0, 0);
////        else if(list.size() >=30 &&list.size() < 60) mChart.zoom(6, 1, 0, 0);
////        else if(list.size() >= 60 && list.size() < 90) mChart.zoom(9, 1, 0, 0);
////        else if(list.size() >= 90 && list.size() <=120) mChart.zoom(12, 1, 0, 0);
////        else mChart.zoom(15, 1, 0, 0);
//
//        //x.setAxisMaximum((float) list.size());
//        //mChart.setHorizontalScrollBarEnabled(true);
//        mChart.setVisibleXRangeMaximum(4);
//        mChart.animateXY(2000, 2000);
//        mChart.invalidate();
    }

    @Override
    public LineDataSet getDataSet(ArrayList<Entry> entryArrayList) {
        LineDataSet set1 = new LineDataSet(entryArrayList, "");

        set1.setDrawValues(true);

        set1.setDrawFilled(true);
        set1.setValueTextColor(getResources().getColor(R.color.colorPrimary));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawCircles(true);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(getResources().getColor(R.color.colorPrimary));
        set1.setFillColor(getResources().getColor(R.color.colorPrimary));
        set1.setFillAlpha(100);
        set1.setDrawHighlightIndicators(false);


        return set1;
    }

    @Override
    public void addToValueToLineView(List<ChartItem> list) {

        if(list != null && list.size() != 0){
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<Integer> listLikes = new ArrayList<>();
            ArrayList<Integer> listComments = new ArrayList<>();
            ArrayList<Integer> listViews = new ArrayList<>();

            for (ChartItem item:list
                    ) {
                dates.add(item.getDates());
                listLikes.add(item.getCountsLikes());
                listComments.add(item.getCountComments());
                listViews.add(item.getCountViews());
            }

            ArrayList<ArrayList<Integer>> arrLikes = new ArrayList<>();
            ArrayList<ArrayList<Integer>> arrComments = new ArrayList<>();
            ArrayList<ArrayList<Integer>> arrViews = new ArrayList<>();

            arrLikes.add(listLikes);
            arrComments.add(listComments);
            arrViews.add(listViews);

            chartLikes.setBottomTextList(dates);
            chartComments.setBottomTextList(dates);
            chartViews.setBottomTextList(dates);

            chartLikes.setDataList(arrLikes);
            chartComments.setDataList(arrComments);
            chartViews.setDataList(arrViews);
        }else Toast.makeText(context, getResources().getString(R.string.empty_list),Toast.LENGTH_SHORT).show();


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
    public void showSnackUpdate() {
        Snackbar snackbar = Snackbar.make(img_filter,
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
    public void showNoInternetConnection() {
        Toast.makeText(v.getContext(), getResources().getString(R.string.err_connection_internet),
                Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {

        int id = view.getId();
        animClickCard(view);
        switch (id){
            case R.id.card_all:
                setSelectedCard(card_media_all);
                _presenter.sortTypeMedia(TypePosts.All);
                typePosts = TypePosts.All;
                break;
            case R.id.card_photo:
                setSelectedCard(card_media_photo);
                _presenter.sortTypeMedia(TypePosts.Photo);
                typePosts = TypePosts.Photo;
                break;
            case R.id.card_video:
                setSelectedCard(card_media_video);
                _presenter.sortTypeMedia(TypePosts.Video);
                typePosts = TypePosts.Video;
                break;
            case R.id.card_slider:
                setSelectedCard(card_media_slider);
                _presenter.sortTypeMedia(TypePosts.Slider);
                typePosts = TypePosts.Slider;
                break;
        }


    }

    @Override
    public void clickApply() {
        String d = "";
    }

    @Override
    public void onStop() {
        _presenter.destroyListeners();
        super.onStop();
    }

    @Override
    public void onResume() {
        _presenter.chechInternet();
        super.onResume();
    }
}
