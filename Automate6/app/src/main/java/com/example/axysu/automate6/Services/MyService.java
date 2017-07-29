package com.example.axysu.automate6.Services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.AlarmActivity;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by axysu on 7/17/2017.
 */

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private float sysytemCurrentBattery;
    private Rules rule;
    private ArrayList<Rules> arrayList;
    DataBaseAdapter dataBaseAdapter;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    LatLng systemCurrentLatLng;
    FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private static String TAG="MyService";
    private String activity_type;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"onCreate");
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        dataBaseAdapter = new DataBaseAdapter(this);
        systemCurrentLatLng = new LatLng(0,0);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(0)
                .setFastestInterval(0)
                .setSmallestDisplacement(0)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (googleApiClient.isConnected()){
            Log.v(TAG,"googleApiClient.isConnected()OnCreate");
            requestLocationUpdates();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(checkMotion, new IntentFilter("ACTIVITY_UPDATE"));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStart");
        googleApiClient.connect();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                checkForMatch(dataBaseAdapter.getAllData());
        Log.v("rer","Service is running");
            }
        }, 10, 2, TimeUnit.SECONDS);

        if (googleApiClient.isConnected()){
            Log.v(TAG,"googleApiClient.isConnected()Onstart");
            requestLocationUpdates();
        }
        else{
            Log.v(TAG,"googleApiClient.isNotConnected()Onstart");
        }
        return START_STICKY;
    }

    private void checkForMatch(ArrayList<Rules> arrayList) {

        Log.v(TAG,"checkingForRules +"+arrayList.size() );
        for (int i = 0; i < arrayList.size(); i++) {
            Rules current = arrayList.get(i);
//            Log.v(TAG,"activity = "+current.activity);
//            Log.v(TAG,"date = "+current.date);
//            Log.v(TAG,"time = "+current.time);
//            Log.v(TAG,"location = "+current.location);
//            Log.v(TAG,"battery = "+current.battery);
//            Log.v(TAG, String.valueOf(matchDate(current.date))
//                    +String.valueOf(matchTime(current.time))
//                    +String.valueOf(matchLocation(current.location))
//                    +String.valueOf(matchActivity(current.activity))
//                    +String.valueOf(matchBattery(current.battery)));
            if (matchDate(current.date) && matchTime(current.time) && matchLocation(current.location)
                    && matchActivity(current.activity) && matchBattery(current.battery)) {

                onMatchFound(current);
            }
        }

    }

    private boolean matchLocation(String location) {

        if (location.equalsIgnoreCase("-1") )
            return true;
        LatLng latlng = getLocation();
       // LatLng latlng = new LatLng(1, 1);
        String asd[] = location.split(",");
        LatLng temp = new LatLng(Double.parseDouble(asd[0]), Double.parseDouble(asd[1]));
        double distanceSquare = (latlng.latitude - temp.latitude) * (latlng.latitude - temp.latitude) +
                (latlng.longitude - temp.longitude) * (latlng.longitude - temp.longitude);
        return ((distanceSquare) <= 100);
    }

    private boolean matchBattery(int battery) {

        if (battery == -1)
            return true;
        return (battery == this.sysytemCurrentBattery);
    }

    private boolean matchActivity(String activity) {

        if (activity.equalsIgnoreCase("-1"))
            return true;
        return (activity.contains(getActivity()));
    }

    private boolean matchTime(String time) {

        if (time.equalsIgnoreCase("-1"))
            return true;
        Calendar c = Calendar.getInstance();
        int currhr = c.get(Calendar.HOUR);
        int currmin = c.get(Calendar.MINUTE);
        if (Integer.parseInt(time.substring(0, 2)) == (currhr) &&
                Integer.parseInt(time.substring(3, 5)) >= currmin - 5 &&
                Integer.parseInt(time.substring(3, 5)) <= currmin + 5)
            return true;
        else
            return false;
    }

    public boolean matchDate(String date) {

        if (date.equalsIgnoreCase("-1"))
            return true;
        Calendar c = Calendar.getInstance();
        int currday = c.get(Calendar.DATE);
        int currmonth = c.get(Calendar.MONTH) + 1;
        int curryear = c.get(Calendar.YEAR);
        if (Integer.parseInt(date.substring(0, 2)) == currday &&
                Integer.parseInt(date.substring(3, 5)) == currmonth &&
                Integer.parseInt(date.substring(6)) == curryear)
            return true;
        else
            return false;

    }


    public void onMatchFound(Rules current) {

        Log.v(TAG,"Match");
        if (current.airplaneMode != -1)
            toggleAirplaneMode(current.airplaneMode);
        if (current.music != -1)
            startMusic(current.music);
        if (current.mobileData != -1)
            toggleAirplaneMode(current.mobileData);
        if (current.wifi != -1)
            toggleWifi(current.wifi);
        if (current.silent != -1)
            toggleAirplaneMode(current.silent);

        if (!current.alarm.equalsIgnoreCase("-1"))
            startAlarm(current.alarm);
        if (!current.notification.equalsIgnoreCase("-1"))
            sendNotification(current.notification);
        if (!current.phonecall.equalsIgnoreCase("-1"))
            startCall(current.phonecall);


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
        Log.v(TAG,"Starting alarm");
        Intent intent = new Intent(this, AlarmActivity.class);
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

    public void toggleAirplaneMode(int flag) {

    }

    public void toggleWifi(int flag) {

        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
        else
            wifiManager.setWifiEnabled(false);


    }

    public void toggleMobileData(int flag) {

    }

    public void toggleSilentMode(int flag) {

    }

    public void startMusic(int flag) {

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        sharedPreferences = getSharedPreferences("Settings",0);

        if (!audioManager.isMusicActive()){

            Log.v(TAG,"launch player");
             final String finalDefaultMusicAppPackage =  sharedPreferences.getString(FetchDataForRulesLists.DEFAULTMUSICPACKAGE,"com.google.android.music");
            if(finalDefaultMusicAppPackage != null){

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(finalDefaultMusicAppPackage);

                if(launchIntent == null){
                    return;
                }

                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(launchIntent,201);

                CountDownTimer countDownTimer = new CountDownTimer(2000,3000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v(TAG,millisUntilFinished+"");
                    }

                    @Override
                    public void onFinish() {
                        playMusiconApp(finalDefaultMusicAppPackage);
                    }
                };



                //  sendEvent(keyCode, finalDefaultMusicAppPackage);

            }

        }

    }

    private void playMusiconApp(String defaultMusicAppPackage){
        Log.v(TAG,"inside music player activity" + defaultMusicAppPackage);
        Intent intent;
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(defaultMusicAppPackage);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        getApplicationContext().sendOrderedBroadcast(intent, null);

        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(defaultMusicAppPackage);
        intent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
        getApplicationContext().sendOrderedBroadcast(intent, null);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.v(TAG,level+"");
            sysytemCurrentBattery = level;

        }
    };


    private String getActivity() {

        return activity_type;
    }

    private LatLng getLocation() {

        return systemCurrentLatLng;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG,"onConnected");
        requestLocationUpdates();
        requestActivityUpdatesButtonHandler();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.v(TAG,"requestlocationupdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG,"onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.v(TAG,"LOCATION CHANGED"+location.getLatitude()+","+location.getLongitude());
        systemCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//        Intent intent = new Intent();
//        intent.setAction("com.akshaysuman.CUSTOM_INTENT");
//        sendBroadcast(intent);

    }

    public void requestActivityUpdatesButtonHandler() {
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this, "GoogleApiClient not yet connected. Try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                googleApiClient,
                0,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, SpeedService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    private BroadcastReceiver checkMotion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
               // Log.v(TAG,"inside");
                activity_type = getType(Integer.parseInt(intent.getStringExtra("activity")));
                int confidence = Integer.parseInt(intent.getStringExtra("percentage"));

           // Log.v(TAG,activity_type);
           // Log.v(TAG,""+confidence);


        }
    };

    String getType(int type) {
        switch (type) {
            case DetectedActivity.STILL:
                return "Idle";
            case DetectedActivity.IN_VEHICLE:
                return "Driving";
            case DetectedActivity.ON_BICYCLE:
                return "Driving";
            case DetectedActivity.ON_FOOT:
                return "Walking";
            case DetectedActivity.RUNNING:
                return "Running";
            case DetectedActivity.TILTING:
                return "Tilting";
            case DetectedActivity.UNKNOWN:
                return "Unknown";
            case DetectedActivity.WALKING:
                return "Walking";


        }
        return "";

    }
}
