package com.example.axysu.automate6.Services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.AlarmActivity;
import com.example.axysu.automate6.BroadcastReceivers.MyReceiver;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by axysu on 7/17/2017.
 */

public class MyService extends Service {

    private float battery;
    private Rules rule;
    private ArrayList<Rules> arrayList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                getSystemStatus();
                checkForMatch();

            }
        }, 10, 60, TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    public void getSystemStatus() {

        rule = new Rules();
        getLocation();
        getDate();
        getTime();
        getBatteryStatus();
        getActivity();
    }

    public void checkForMatch() {

        arrayList = new ArrayList<>();
        arrayList = new DataBaseAdapter(this).getDataByTriggers(rule.date,rule.time,rule.location,rule.activity,rule.battery);
    }

    public void onMatchFound() {

        if (arrayList!=null && arrayList.size()>0){

            for (int i=0;i<arrayList.size();i++){

                Rules current = arrayList.get(i);



            }
        }

    }

    public void getLocation() {
        //return location;
    }

    private void getDate() {

        Calendar c = Calendar.getInstance();
        rule.date = String.valueOf(c.get(Calendar.DATE)) + "/" + String.valueOf(c.get(Calendar.MONTH))
                + "/" + String.valueOf(c.get(Calendar.YEAR));
    }

    public void getTime() {

        Calendar c = Calendar.getInstance();
        rule.time = String.valueOf(c.get(Calendar.HOUR)) + ":" + String.valueOf(c.get(Calendar.MINUTE));
    }

    public void getBatteryStatus() {

        rule.battery = (int) this.battery;

    }

    public void getActivity() {

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battery = level;

        }
    };

    public void sendNotification(String message) {

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle("My notification")
                        .setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

    }

    public void startAlarm(String message) {

        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("MESSAGE", message);
        startActivity(intent);

    }

    public void startCall(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public void toggleAirplaneMode(boolean flag){

    }
    public void toggleWifi(boolean flag){

    }
    public void toggleMobileData(boolean flag){

    }
    public void toggleSilentMode(boolean flag){

    }
    public void startMusic(){

    }


}
