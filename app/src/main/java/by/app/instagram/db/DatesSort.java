package by.app.instagram.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.model.pui.ChartItem;

public class DatesSort {

    private static String TAG = "DATES SORT";

    public List<ChartItem> getDatesFilterAll(List<ChartItem> list){

        List<ChartItem> sort = new ArrayList<>();

        ArrayList<String> arrayList = new ArrayList<>();

        for (ChartItem chart : list
             ) {
             if(list.size() <= 6){
                 String m_date = chart.getDates().split("-")[0]+ "-"+
                         chart.getDates().split("-")[1]+"-"+
                         chart.getDates().split("-")[2] +" | ";
                 arrayList.add(m_date);
                 ChartItem chartItem = new ChartItem();
                 chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                         chart.getCountComments(), chart.getCountViews());
                 sort.add(chartItem);

             }else {
                 String m_date = chart.getDates().split("-")[0]+ "-"+
                         chart.getDates().split("-")[1]+ " | ";
                 if(!arrayList.contains(m_date)){
                     arrayList.add(m_date);
                     ChartItem chartItem = new ChartItem();
                     chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                             chart.getCountComments(), chart.getCountViews());
                     sort.add(chartItem);
                 }else {

                     sort.get(sort.size() -1 ).addCountLikes(chart.getCountsLikes());
                     sort.get(sort.size() -1 ).addCountComments(chart.getCountComments());
                     sort.get(sort.size() -1 ).addCountViews(chart.getCountViews());
                 }
             }

        }
//        ChartItem d = new ChartItem();
//        d.setAllValue(0, "",0,0,0);
//        sort.add(d);



        for (ChartItem item: sort
             ) {
            Log.e(TAG, String.valueOf(item.getCountsLikes()));
            Log.e(TAG, String.valueOf(item.getDates()));

        }

        Log.e(TAG, "dates arr string size= "+arrayList.size()+" list size = "+list.size());

        return sort;
    }

    public List<ChartItem> getDatesFilterPhoto(List<ChartItem> list){
        List<ChartItem> sort = new ArrayList<>();

        ArrayList<String> arrayList = new ArrayList<>();

        for (ChartItem chart : list
                ) {
            if(chart.getMediaType() == 1){
                if(list.size() <= 6){
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+"-"+
                            chart.getDates().split("-")[2] +" | ";
                    arrayList.add(m_date);
                    ChartItem chartItem = new ChartItem();
                    chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                            chart.getCountComments(), chart.getCountViews());
                    sort.add(chartItem);

                }else {
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+ " | ";
                    if(!arrayList.contains(m_date)){
                        arrayList.add(m_date);
                        ChartItem chartItem = new ChartItem();
                        chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                                chart.getCountComments(), chart.getCountViews());
                        sort.add(chartItem);
                    }else {

                        sort.get(sort.size() -1 ).addCountLikes(chart.getCountsLikes());
                        sort.get(sort.size() -1 ).addCountComments(chart.getCountComments());
                        sort.get(sort.size() -1 ).addCountViews(chart.getCountViews());
                    }
                }
            }

        }

        return sort;
    }

    public List<ChartItem> getDatesFilterVideo(List<ChartItem> list){
        List<ChartItem> sort = new ArrayList<>();

        ArrayList<String> arrayList = new ArrayList<>();

        for (ChartItem chart : list
                ) {
            if(chart.getMediaType() == 2){
                if(list.size() <= 6){
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+"-"+
                            chart.getDates().split("-")[2] +" | ";
                    arrayList.add(m_date);
                    ChartItem chartItem = new ChartItem();
                    chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                            chart.getCountComments(), chart.getCountViews());
                    sort.add(chartItem);

                }else {
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+ " | ";
                    if(!arrayList.contains(m_date)){
                        arrayList.add(m_date);
                        ChartItem chartItem = new ChartItem();
                        chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                                chart.getCountComments(), chart.getCountViews());
                        sort.add(chartItem);
                    }else {

                        sort.get(sort.size() -1 ).addCountLikes(chart.getCountsLikes());
                        sort.get(sort.size() -1 ).addCountComments(chart.getCountComments());
                        sort.get(sort.size() -1 ).addCountViews(chart.getCountViews());
                    }
                }
            }

        }

        return sort;
    }

    public List<ChartItem> getDatesFilterSlider(List<ChartItem> list){
        List<ChartItem> sort = new ArrayList<>();

        ArrayList<String> arrayList = new ArrayList<>();

        for (ChartItem chart : list
                ) {
            if(chart.getMediaType() == 8){
                if(list.size() <= 6){
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+"-"+
                            chart.getDates().split("-")[2] +" | ";
                    arrayList.add(m_date);
                    ChartItem chartItem = new ChartItem();
                    chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                            chart.getCountComments(), chart.getCountViews());
                    sort.add(chartItem);

                }else {
                    String m_date = chart.getDates().split("-")[0]+ "-"+
                            chart.getDates().split("-")[1]+ " | ";
                    if(!arrayList.contains(m_date)){
                        arrayList.add(m_date);
                        ChartItem chartItem = new ChartItem();
                        chartItem.setAllValue(chart.getMediaType(), m_date, chart.getCountsLikes(),
                                chart.getCountComments(), chart.getCountViews());
                        sort.add(chartItem);
                    }else {

                        sort.get(sort.size() -1 ).addCountLikes(chart.getCountsLikes());
                        sort.get(sort.size() -1 ).addCountComments(chart.getCountComments());
                        sort.get(sort.size() -1 ).addCountViews(chart.getCountViews());
                    }
                }
            }

        }

        return sort;
    }

}
