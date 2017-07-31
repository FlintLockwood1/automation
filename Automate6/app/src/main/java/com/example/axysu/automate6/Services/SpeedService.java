package com.example.axysu.automate6.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.axysu.automate6.MainActivity;
import com.example.axysu.automate6.R;
import com.google.android.gms.location.ActivityRecognitionResult;

public class SpeedService extends IntentService {

    private static String TAG="SpeedService";
    private static final int NOTIFICATION_ID=2;
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
        displayForegroundNotification();
        Log.v(TAG,"onCreate");
    }

    private void displayForegroundNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_manage)
                        .setContentTitle("System Cant Kill Me")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("We Get All Ur Activities");

//        if (getFileStructure() != null) {
//            String title = Utils.filter(getFileStructure().getTitle());
//            if (title.length() > 45)
//                title = title.substring(0, 44);
//            builder.setContentText(Utils.filter(title));
//        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(false);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
        startForeground(NOTIFICATION_ID, builder.build());
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
