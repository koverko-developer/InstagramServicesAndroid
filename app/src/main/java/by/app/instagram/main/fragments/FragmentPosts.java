package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.R;
import by.app.instagram.enums.TypePosts;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.main.presenters.PostsPresenter;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;

@SuppressLint("ValidFragment")
public class FragmentPosts extends Fragment implements PostsContract.ViewModel,
        View.OnClickListener {

    private static String TAG = FragmentPosts.class.getName();

    View v;
    Context context;
    PostsPresenter _presenter;

    CardView card_3, card_media_all, card_media_photo, card_media_video, card_media_slider;
    TextView tv_count_likes, tv_count_comments, tv_count_views, tv_count_all,
             tv_count_photo, tv_count_video, tv_count_slider;

    LineChart mChart;
    CardView progress;

    public FragmentPosts(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.posts_fragment, container, false);
        initChart();
        initCardH();
        initCardTypeMedia();

        if(_presenter == null) _presenter = new PostsPresenter(this, v.getContext());

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initChart() {
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i< 1; i++){
//            float val = (float) (Math.random() * 5) + 20;
            yVals.add(new Entry(i, 0));
        }

        mChart = v.findViewById(R.id.chartLikes);


        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBorders(false);
        //mChart.zoomToCenter(10f, 0f);
        mChart.getAxisRight().setEnabled(false);
        YAxis y = mChart.getAxisLeft();
        y.setDrawGridLines(false);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mChart.getLegend().setEnabled(false);

        mChart.setViewPortOffsets(40f, 32f, 20f, 0);
        mChart.getDescription().setEnabled(false);

        XAxis x = mChart.getXAxis();
        x.setDrawGridLines(false);

        //x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        x.setTextSize(10f);
        x.setLabelRotationAngle(-45f);


        LineDataSet set1;
        set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setDrawValues(true);
        set1.setDrawFilled(true);

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
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData data = new LineData(set1);
        mChart.setData(data);
        try {
            mChart.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void onClick(View view) {

        int id = view.getId();
        animClickCard(view);
        switch (id){
            case R.id.card_all:
                setSelectedCard(card_media_all);
                _presenter.sortTypeMedia(TypePosts.All);
                break;
            case R.id.card_photo:
                setSelectedCard(card_media_photo);
                _presenter.sortTypeMedia(TypePosts.Photo);
                break;
            case R.id.card_video:
                setSelectedCard(card_media_video);
                _presenter.sortTypeMedia(TypePosts.Video);
                break;
            case R.id.card_slider:
                setSelectedCard(card_media_slider);
                _presenter.sortTypeMedia(TypePosts.Slider);
                break;
        }


    }
}
