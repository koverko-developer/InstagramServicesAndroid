package by.app.instagram.db;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.app.instagram.model.pui.ChartItem;

public class PostFilterSort {

    private static String TAG = PostFilterSort.class.getName();

    public static List<ChartItem> getCurrentMonth(List<ChartItem> list){

        Calendar dateAndTime = Calendar.getInstance();
        long current_month = dateAndTime.getTimeInMillis();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM",Locale.getDefault());
        String current_day_and_month = df.format(current_month);

        List<ChartItem> sortList = new ArrayList<>();

        for (ChartItem item : list
             ) {

            if(item.getDates().contains(current_day_and_month)) sortList.add(item);

        }

        Log.e(TAG, "current month date list size = "+sortList.size());

        return sortList;
    }

    public static List<ChartItem> getSelectedMonth(List<ChartItem> list,
                                                   Long period_1, Long period_2){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

        List<ChartItem> sortList = new ArrayList<>();

        for (ChartItem item : list
                ) {

            try {
                Date d = df.parse(item.getDates());
                long millis = d.getTime();

                Log.e(TAG, "item date long = " + millis + " period_1 = "+ period_1);

                if(item.getTakenAt() >= period_1 && item.getTakenAt() <= period_2) sortList.add(item);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Log.e(TAG, "selected month date list size = "+sortList.size());

        return sortList;
    }

    public static List<ChartItem> getCountsPosts(List<ChartItem> list, int count){

        List<ChartItem> sortList = new ArrayList<>();

        for(int i = 0; i < count; i++){
            sortList.add(list.get(i));
        }

        Log.e(TAG, "counts posts list size = "+sortList.size());

        return sortList;
    }

}
