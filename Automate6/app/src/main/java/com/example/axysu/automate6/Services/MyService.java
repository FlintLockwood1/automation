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
import com.google.android.gms.maps.model.LatLng;

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
    DataBaseAdapter dataBaseAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        dataBaseAdapter = new DataBaseAdapter(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                checkForMatch(dataBaseAdapter.getAllData());

            }
        }, 10, 60, TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    private void checkForMatch(ArrayList<Rules> arrayList) {
        
        for (int i=0;i<arrayList.size();i++){
            Rules current = arrayList.get(i);
            if (matchDate(current.date) && matchTime (current.time)&& matchLocation(current.location)
                    && matchActivity(current.activity) && matchBattery(current.battery)){

                onMatchFound(current);
            }
        }

    }

    private boolean matchLocation(String location) {
        //LatLng latlng = getLocation();
        LatLng latlng = new LatLng(1,1);
        String asd[] = location.split(",");
        LatLng temp = new LatLng(Double.parseDouble(asd[0]),Double.parseDouble(asd[1]));
        double distanceSquare = (latlng.latitude-temp.latitude)   * (latlng.latitude-temp.latitude) +
                (latlng.longitude-temp.longitude) * (latlng.longitude-temp.longitude);
        return((distanceSquare) <= 100);
    }

    private boolean matchBattery(int battery) {
        return (battery==this.battery);
    }


    private boolean matchActivity(String activity) {

        return (activity.contains(getActivity()));
    }

    private boolean matchTime(String time) {
        Calendar c = Calendar.getInstance();
        int currhr = c.get(Calendar.HOUR);
        int currmin = c.get(Calendar.MINUTE);
        if (Integer.parseInt(time.substring(0,2))== (currhr) &&
                Integer.parseInt(time.substring(3, 5)) >= currmin-5 &&
                Integer.parseInt(time.substring(3, 5)) <= currmin+5)
            return true;
        else
            return false;
    }

    public boolean matchDate(String date){

        Calendar c = Calendar.getInstance();
        int currday = c.get(Calendar.DATE);
        int currmonth = c.get(Calendar.MONTH)+1;
        int curryear = c.get(Calendar.YEAR);
        if (Integer.parseInt(date.substring(0, 2))== currday &&
                Integer.parseInt(date.substring(3, 5))== currmonth &&
                Integer.parseInt(date.substring(6))== curryear)
            return true;
        else
            return false;

    }




    public void onMatchFound(Rules current) {

        if (current.airplaneMode!=-1)
            toggleAirplaneMode(current.airplaneMode);
        if (current.music!=-1)
            toggleAirplaneMode(current.music);
        if (current.mobileData!=-1)
            toggleAirplaneMode(current.mobileData);
        if (current.wifi!=-1)
            toggleAirplaneMode(current.wifi);
        if (current.silent!=-1)
            toggleAirplaneMode(current.silent);

        if (!current.alarm.equalsIgnoreCase("DEFAULT"))
            startAlarm(current.alarm);
        if (!current.notification.equalsIgnoreCase("DEFAULT"))
            startAlarm(current.notification);
        if (!current.phonecall.equalsIgnoreCase("DEFAULT"))
            startAlarm(current.phonecall);


    }



    private String getActivity() {

        return "walking";
    }

    private LatLng getLocation(){

        return null;
    }

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

        Intent intent = new Intent(this,AlarmActivity.class);
        //startActivityForResult(intent,0);

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

    public void toggleAirplaneMode(int flag){

    }
    public void toggleWifi(int flag){

    }
    public void toggleMobileData(int flag){

    }
    public void toggleSilentMode(int flag){

    }
    public void startMusic(int flag){

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battery = level;

        }
    };


}
