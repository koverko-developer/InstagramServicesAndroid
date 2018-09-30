package by.app.instagram.db;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.pui.ChartItem;

public class AudienceDatesSort {

    private static String TAG = AudienceDatesSort.class.getName();

    public static List<ChartItem> getAudience(List<AudienceObject> list){

        ArrayList<String> arrayList = new ArrayList<>();
        List<ChartItem> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            if(arrayList.contains(item.getAt())) {
                int i = arrayList.indexOf(item.getAt());
                sortList.get(i).setCount(sortList.get(i).getCount() + 1);
            }
            else {
                arrayList.add(item.getAt());
                ChartItem itemChart = new ChartItem();
                itemChart.setDates(item.getAt());
                itemChart.setCount(1);
                sortList.add(itemChart);
            }

        }

        Log.e(TAG, "follow list = "+sortList.size());

        return sortList;
    }

    public static List<ChartItem> getSelectedMonth(List<AudienceObject> list,
                                                   Long period_1, Long period_2){

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        ArrayList<String> arrayList = new ArrayList<>();
        List<ChartItem> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            try {
                Date d = df.parse(item.getAt());
                long millis = d.getTime();

                Log.e(TAG, "item date long = " + millis + " period_1 = "+ period_1);

                if(millis >= period_1 && millis <= period_2) {

                    if(arrayList.contains(item.getAt())) {
                        int i = arrayList.indexOf(item.getAt());
                        sortList.get(i).setCount(sortList.get(i).getCount() + 1);
                    }
                    else {
                        arrayList.add(item.getAt());
                        ChartItem itemChart = new ChartItem();
                        itemChart.setDates(item.getAt());
                        itemChart.setCount(1);
                        sortList.add(itemChart);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Log.e(TAG, "selected month date list size = "+sortList.size());

        return sortList;
    }

    public static List<ChartItem> getCurrentMonth(List<AudienceObject> list){

        Calendar dateAndTime = Calendar.getInstance();
        long current_month = dateAndTime.getTimeInMillis();
        ArrayList<String> arrayList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy",Locale.getDefault());
        String current_day_and_month = df.format(current_month);

        List<ChartItem> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            if(item.getAt().contains(current_day_and_month)) {
                if(arrayList.contains(item.getAt())) {
                    int i = arrayList.indexOf(item.getAt());
                    sortList.get(i).setCount(sortList.get(i).getCount() + 1);
                }
                else {
                    arrayList.add(item.getAt());
                    ChartItem itemChart = new ChartItem();
                    itemChart.setDates(item.getAt());
                    itemChart.setCount(1);
                    sortList.add(itemChart);
                }
            }

        }

        Log.e(TAG, "current month date list size = "+sortList.size());

        return sortList;
    }

    public static List<AudienceObject> getAudienceUsers(List<AudienceObject> list){

        ArrayList<String> arrayList = new ArrayList<>();
        List<AudienceObject> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            sortList.add(item);

        }

        Log.e(TAG, "follow list users = "+sortList.size());

        return sortList;
    }

    public static List<AudienceObject> getSelectedMonthUsers(List<AudienceObject> list,
                                                   Long period_1, Long period_2){

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        ArrayList<String> arrayList = new ArrayList<>();
        List<AudienceObject> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            try {
                Date d = df.parse(item.getAt());
                long millis = d.getTime();

                Log.e(TAG, "item date long = " + millis + " period_1 = "+ period_1);

                if(millis >= period_1 && millis <= period_2) {

                    sortList.add(item);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Log.e(TAG, "selected month date list size users = "+sortList.size());

        return sortList;
    }

    public static List<AudienceObject> getCurrentMonthUsers(List<AudienceObject> list){

        Calendar dateAndTime = Calendar.getInstance();
        long current_month = dateAndTime.getTimeInMillis();
        ArrayList<String> arrayList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy",Locale.getDefault());
        String current_day_and_month = df.format(current_month);

        List<AudienceObject> sortList = new ArrayList<>();

        for (AudienceObject item : list
                ) {

            if(item.getAt().contains(current_day_and_month)) {
                sortList.add(item);
            }

        }

        Log.e(TAG, "current month date list size users = "+sortList.size());

        return sortList;
    }

}
