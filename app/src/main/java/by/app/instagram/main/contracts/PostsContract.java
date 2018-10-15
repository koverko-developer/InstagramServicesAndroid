package by.app.instagram.main.contracts;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.enums.TypePosts;
import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.view.filter.TypeSpinnerFilter;

public class PostsContract {

    public interface ViewModel {
        void initChart();
        void initCardH();
        void initCardTypeMedia();
        void initChartLikes();
        void initChartComments();
        void initChartViews();
        void initFilter();
        void initAppBar();
        void cardAnimation(View v);
        void setValueToCardH(UserInfoMedia media);
        void setInfoSortTypePost(UserInfoMedia media);
        void setSelectedCard(CardView card);
        void animClickCard(View v);
        void animText(View v);
        void showProgress();
        void hideProgress();
        void hideFilter();
        void showFilter();
        void addDataToChartLikes(List<ChartItem> list);
        LineDataSet getDataSet(ArrayList<Entry> entryArrayList);
        void addToValueToLineView(List<ChartItem> list);
        void animRotate(View view, int type);
    }

    public interface Presenter {
        void getUI();
        void getPostsInfo();
        void checkMediaRange(Long count_media);
        void sortTypeMedia(TypePosts typePosts);
        void setTypeSpinnerFilter(TypeSpinnerFilter typeSpinnerFilter);
        void setCountsPosts(int _count);
        void setPeriod1(Long _period1);
        void setPeriod2(Long _perio2);
        void addListenerProgress();
        void addListenerInfo();
        void destroyListeners();

    }

}
