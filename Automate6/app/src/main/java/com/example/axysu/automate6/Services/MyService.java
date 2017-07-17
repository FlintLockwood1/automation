package com.example.axysu.automate6.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.axysu.automate6.BroadcastReceivers.MyReceiver;
import com.example.axysu.automate6.Objects.Rules;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerReceiver(this.mBatInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
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
        },10,60,TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    public void getSystemStatus(){

        rule = new Rules();
        getLocation();
        getDate();
        getTime();
        getBatteryStatus();
        getActivity();
    }

    public void checkForMatch(){

    }

    public void onMatchFound(){

    }

    public void getLocation() {
        //return location;
    }
    private void getDate() {

        Calendar c = Calendar.getInstance();
        rule.date = String.valueOf(c.get(Calendar.DATE)) + "/" + String.valueOf(c.get(Calendar.MONTH))
                + "/" + String.valueOf(c.get(Calendar.YEAR));
    }
    public void getTime(){

        Calendar c = Calendar.getInstance();
        rule.time = String.valueOf(c.get(Calendar.HOUR))+ ":" + String.valueOf(c.get(Calendar.MINUTE));
    }
    public void getBatteryStatus(){

        rule.battery = (int) this.battery;

    }
    public void getActivity(){

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battery = level;

        }
    };


}
