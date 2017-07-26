package com.example.axysu.automate6.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

public class SpeedService extends IntentService {

    private static String TAG="SpeedService";
    public SpeedService() {
        super("SpeedService");
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //System.out.println("Acctivy recognition service started");
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            Intent speedIntent = new Intent("ACTIVITY_UPDATE");
            speedIntent.putExtra("activity", result.getMostProbableActivity().getType() + "");
            speedIntent.putExtra("percentage", result.getMostProbableActivity().getConfidence() + "");
            LocalBroadcastManager.getInstance(this).sendBroadcast(speedIntent);
        }
    }
}
