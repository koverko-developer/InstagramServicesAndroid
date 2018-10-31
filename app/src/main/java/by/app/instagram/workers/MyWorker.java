package by.app.instagram.workers;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

public class MyWorker extends Worker{

    static final String TAG = "myworker";

    @NonNull
    @Override
    public WorkerResult doWork() {

        Log.e(TAG, "start");

        try{

        }catch (Exception e){

        }

        Log.e(TAG, "finish");

        return WorkerResult.SUCCESS;
    }
}
