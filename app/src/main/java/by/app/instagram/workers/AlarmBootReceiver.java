package by.app.instagram.workers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmBootReceiver extends BroadcastReceiver{

    private static String TAG = AlarmBootReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Log.e(TAG, "ONRECEIVER BOOOT COMPLETED");
        }
    }
}
