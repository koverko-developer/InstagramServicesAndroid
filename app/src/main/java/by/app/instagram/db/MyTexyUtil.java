package by.app.instagram.db;

import android.util.Log;

public class MyTexyUtil {

    private static String TAG  = MyTexyUtil.class.getName();

    public static String countsInt(int count){

        String s = "";

        if(count > 100000  && count < 1000000) {

            int i = count / 1000;
            s = i + "K";

        }else if(count >= 1000000){
            int i =  count / 1000000;
            s = i + "M";
        }else s = String.valueOf(count);

        return s;
    }

    public static String countsLong(long count){
        String s = "";

        if(count > 100000  && count < 1000000) {

            int i =  (int) count / 1000;
            s = i + "K";

        }else if(count >= 1000000){
            int i = (int) count / 1000000;
            s = i + "M";
        }else s = String.valueOf(count);

        return s;
    }
}
