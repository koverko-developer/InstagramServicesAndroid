package by.app.instagram.db;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.pui.ChartItem;

public class FeedSort {

    private static String TAG = FeedSort.class.getName();

    public static List<MediaObject> sortCountPosts(int count, List<MediaObject> _list){

        if(count > _list.size()) count = _list.size();

        List<MediaObject> sortList = new ArrayList<>();

        for (int i = 0; i < count; i++){
            sortList.add(_list.get(i));
        }


        Log.e(TAG, "count posts sort = "+sortList.size());
        return sortList;
    }

    public static List<MediaObject> sortSelectMonth(List<MediaObject> _list, long _period1,
                                                    long _period2){

        List<MediaObject> sortList = new ArrayList<>();

        for (MediaObject item : _list
                ) {

            long millis = item.getTakenAt();
            Log.e(TAG, "item date long = " + millis + " period_1 = "+ _period1);

            if(millis >= _period1 && millis <= _period2) {

                sortList.add(item);
            }

        }

        Log.e(TAG, "select month sort = "+sortList.size());
        return sortList;
    }

    public static List<MediaObject> sortCurrentMonth(List<MediaObject> _list){

        List<MediaObject> sortList = new ArrayList<>();

        Calendar dateAndTime = Calendar.getInstance();
        long current_month = dateAndTime.getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy",Locale.getDefault());
        String current_day_and_month = df.format(current_month);


        for (MediaObject item : _list
                ) {

            String i_date = df.format(item.getTakenAt());

            if(i_date.contains(current_day_and_month)) {
                sortList.add(item);
            }

        }

        Log.e(TAG, "current month sort = "+sortList.size());
        return sortList;
    }

}
