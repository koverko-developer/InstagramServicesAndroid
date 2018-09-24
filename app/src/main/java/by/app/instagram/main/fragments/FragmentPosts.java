package by.app.instagram.main.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import by.app.instagram.R;
import by.app.instagram.main.contracts.PostsContract;
import by.app.instagram.main.presenters.PostsPresenter;
import by.app.instagram.model.fui.UserInfoMedia;

@SuppressLint("ValidFragment")
public class FragmentPosts extends Fragment implements PostsContract.ViewModel{

    View v;
    Context context;
    PostsPresenter _presenter;

    CardView card_3, card_media_all, card_media_photo, card_media_video, card_media_slider;
    TextView tv_count_likes, tv_count_comments, tv_count_views, tv_count_all,
             tv_count_photo, tv_count_video, tv_count_slider;

    LineChart mChart;

    public FragmentPosts(Context context) {
        this.context = context;
    }

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
        mChart.zoomToCenter(10f, 0f);
        mChart.getAxisRight().setEnabled(false);
        YAxis y = mChart.getAxisLeft();
        y.setDrawGridLines(false);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mChart.getLegend().setEnabled(false);

        mChart.setViewPortOffsets(40f, 0, 0, 0);
        mChart.getDescription().setEnabled(false);

        XAxis x = mChart.getXAxis();

        x.setEnabled(false);

        LineDataSet set1;
        set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setDrawValues(false);
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

    }
}
