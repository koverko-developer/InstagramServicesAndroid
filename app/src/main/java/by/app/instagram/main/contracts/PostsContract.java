package by.app.instagram.main.contracts;

import android.support.v7.widget.CardView;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.enums.TypePosts;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;

public class PostsContract {

    public interface ViewModel {
        void initChart();
        void initCardH();
        void initCardTypeMedia();
        void cardAnimation(View v);
        void setValueToCardH(UserInfoMedia media);
        void setInfoSortTypePost(UserInfoMedia media);
        void setSelectedCard(CardView card);
        void animClickCard(View v);
        void animText(View v);
        void showProgress();
        void hideProgress();
        void addDataToChartLikes(List<ChartItem> list);
        LineDataSet getDataSet(ArrayList<Entry> entryArrayList);
    }

    public interface Presenter {
        void getUI();
        void getPostsInfo();
        void checkMediaRange(Long count_media);
        void sortTypeMedia(TypePosts typePosts);

    }

}
