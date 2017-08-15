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
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.AlarmActivity;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.MainActivity;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by axysu on 7/17/2017.
 */

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private float sysytemCurrentBattery;
    DataBaseAdapter dataBaseAdapter;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    LatLng systemCurrentLatLng;
    FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private static String TAG="MyService";
    private String activity_type;
    SharedPreferences sharedPreferences;
    MediaPlayer player;
    boolean musicActive = false;
    boolean alarmActive = false;
    boolean notificationActive = false;
    boolean phonecallActive = false;
    /// ALL Events will fire after 1 min

    private static final int NOTIFICATION_ID=2;
    private ArrayList<Rules> arrayList = new ArrayList<>();
    private HashMap<String,Rules> hashMap = new HashMap();
    private ArrayList<Rules> queue = new ArrayList<>();
    private int ringerMode;
    private int ringerVolume;
    private int dndMode;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataBaseAdapter = new DataBaseAdapter(this);
        displayForegroundNotification();
        populateRulesArrayMapQueue();
        Log.v(TAG,"onCreate");
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        dataBaseAdapter = new DataBaseAdapter(this);
        startLocation();
        LocalBroadcastManager.getInstance(this).registerReceiver(checkMotion, new IntentFilter("ACTIVITY_UPDATE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmReceiver,new IntentFilter("ALARMSTOPPED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(insertRuleReceiver,new IntentFilter("INSERTEDROW"));
        LocalBroadcastManager.getInstance(this).registerReceiver(deleteRuleReceiver,new IntentFilter("DELETEDROW"));
        LocalBroadcastManager.getInstance(this).registerReceiver(updateRuleReceiver,new IntentFilter("UPDATEDROW"));

    }

    private void populateRulesArrayMapQueue() {

        arrayList = dataBaseAdapter.getAllData();
        for (Rules current : arrayList){

            if (current.state.equalsIgnoreCase("active"))
                queue.add(current);
        }
    }

    private void startLocation() {

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
    }

    private void displayForegroundNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_slideshow)
                        .setContentTitle("System Cant Kill Me")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("Foreground");

//        if (getFileStructure() != null) {
//            String title = Utils.filter(getFileStructure().getTitle());
//            if (title.length() > 45)
//                title = title.substring(0, 44);
//            builder.setContentText(Utils.filter(title));
//        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(

                this, 23, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(false);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStart");
        googleApiClient.connect();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                checkForMatch();
                Log.v(TAG,"Service is running");
            }
        }, 10, 10, TimeUnit.SECONDS);



        ScheduledExecutorService scheduler1 = Executors.newSingleThreadScheduledExecutor();
        scheduler1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                Log.v(TAG,"Checking Date change");
                enableRule("DATE");

            }
        }, 5, 300, TimeUnit.SECONDS);



        if (googleApiClient.isConnected()){
            Log.v(TAG,"googleApiClient.isConnected()Onstart");
            requestLocationUpdates();
        }
        else{
            Log.v(TAG,"googleApiClient.isNotConnected()Onstart");
        }
        return START_STICKY;
    }

    private void checkForMatch() {

        Log.v(TAG,"checkingForRules +"+queue.size() );
        for (int i = 0; i < queue.size(); i++) {
            Rules current = queue.get(i);
            Log.v(TAG,current.id + " ");
            if(current.state.equalsIgnoreCase("active")) {
                if (matchDate(current.date) && matchTime(current.time) && matchLocation(current.location)
                        && matchActivity(current.activity) && matchBattery(current.battery)) {

                    Log.v(TAG,"only alarm remains and phonecall to check");
                    if((!current.alarm.equalsIgnoreCase("-1") && !alarmActive)||
                            (!current.phonecall.equalsIgnoreCase("-1") && !phonecallActive) || current.alarm.equalsIgnoreCase("-1")
                            || (!current.phonecall.equalsIgnoreCase("-1"))){
                        executeRule(current);
                        hashRule(current);
                        queue.remove(i);
                    }
                }
            }
        }

    }

    private void enableRule(String trigger){

        ArrayList<Rules> tempList = new ArrayList<>();
        while(hashMap.get(trigger)!=null){
            tempList.add(hashMap.remove(trigger));
        }
//        Log.v(TAG,"tempListSize"+tempList.size());
//        Log.v(TAG,"QUEUE SIZE ="+queue.size());
//        Log.v(TAG,"HASH SIZE ="+hashMap.size());
        if (trigger.equalsIgnoreCase("BATTERY")){

            for (Rules disabled: tempList){
                if (!matchBattery(disabled.battery))
                    queue.add(disabled);
                else
                    hashMap.put("BATTERY",disabled);
            }
        }
        if (trigger.equalsIgnoreCase("ACTIVITY")){

            for (Rules disabled: tempList){
                if (!matchActivity(disabled.activity))
                    queue.add(disabled);
                else
                    hashMap.put("ACTIVITY",disabled);
            }
        }
        if (trigger.equalsIgnoreCase("DATE")){

            for (Rules disabled: tempList){
                if (!matchDate(disabled.date))
                    queue.add(disabled);
                else
                    hashMap.put("DATE",disabled);
            }
        }
        if (trigger.equalsIgnoreCase("LOCATION")){

            for (Rules disabled: tempList){
                if (!matchLocation(disabled.location))
                    queue.add(disabled);
                else
                    hashMap.put("LOCATION",disabled);
            }
        }

//        Log.v(TAG,"QUEUE SIZE ="+queue.size());
//        Log.v(TAG,"HASH SIZE ="+hashMap.size());

    }

    private void hashRule(Rules current) {

        if (current.time.equalsIgnoreCase("-1")){
            if (current.battery != -1){
                hashMap.put("BATTERY",current);
                Log.v(TAG,"hashing rule due to BATTERY");
            }else if (!current.location.equalsIgnoreCase("-1")){
                hashMap.put("LOCATION",current);
                Log.v(TAG,"hashing rule due to LOCATION");
            }else if (!current.activity.equalsIgnoreCase("-1")){
                hashMap.put("ACTIVITY",current);
                Log.v(TAG,"hashing rule due to ACTIVITY");
            }else if (!current.date.equalsIgnoreCase("-1")){
                hashMap.put("DATE",current);
                Log.v(TAG,"hashing rule due to DATE");
            }
        }else{
            Log.v(TAG,"no hashing rule due to TIME");
        }

        Log.v(TAG,"QUEUE SIZE ="+queue.size());
        Log.v(TAG,"HASH SIZE ="+hashMap.size());

    }

    private boolean matchLocation(String location) {

        if (location.equalsIgnoreCase("-1") )
            return true;

        LatLng latlng = getLocation();
        // LatLng latlng = new LatLng(1, 1);
        String asd[] = location.split(",");
        LatLng temp = new LatLng(Double.parseDouble(asd[0]), Double.parseDouble(asd[1]));

        Location currentLocation = new Location("current");
        currentLocation.setLongitude(latlng.longitude);
        currentLocation.setLatitude(latlng.latitude);

        Location savedLocation = new Location("saved");
        savedLocation.setLongitude(temp.longitude);
        savedLocation.setLatitude(temp.latitude);

        if(currentLocation.distanceTo(savedLocation)<100){
            return true;
        } else {
            return false;
        }


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

        //Log.v(TAG,"matching Time");
        if (time.equalsIgnoreCase("-1")) {
            Log.v(TAG,"time = "+time);
            return true;
        }
        Calendar c = Calendar.getInstance();
        int currhr = c.get(Calendar.HOUR_OF_DAY);
        //Log.v(c.get(Calendar.AM_PM));
        int currmin = c.get(Calendar.MINUTE);

        int triggerHr = Integer.parseInt(time.substring(0, 2));
        int triggerMin = Integer.parseInt(time.substring(3, 5));
        Log.v(TAG,currhr+"="+""+triggerHr);
        Log.v(TAG,currmin+"="+""+triggerMin);
        if (triggerHr == (currhr) &&
                triggerMin >= currmin - 1 &&
                triggerMin <= currmin + 1)
            return true;
        else
            return false;
    }

    public boolean matchDate(String date) {

        //Log.v(TAG,"matchDate");
        if (date.equalsIgnoreCase("-1"))
            return true;
        Calendar c = Calendar.getInstance();
        int currday = c.get(Calendar.DATE);
        int currmonth = c.get(Calendar.MONTH) + 1;
        int curryear = c.get(Calendar.YEAR);
        Log.v(date.substring(0, 2),"="+currday);
        Log.v(date.substring(3, 5),"="+currmonth);
        Log.v(date.substring(6),"="+curryear);
        if (Integer.parseInt(date.substring(0, 2)) == currday &&
                Integer.parseInt(date.substring(3, 5)) == currmonth &&
                Integer.parseInt(date.substring(6)) == curryear)
            return true;
        else
            return false;

    }

    public void executeRule(Rules current) {

        Log.v(TAG,"executeRule");
        if (current.airplaneMode != -1)
            toggleAirplaneMode(current.airplaneMode);
        if (current.music != -1)
            startMusic(current.music);
        if (current.mobileData != -1)
            toggleAirplaneMode(current.mobileData);
        if (current.wifi != -1)
            toggleWifi(current.wifi);
        if (current.silent != -1)
            toggleSilentMode(current.silent);

        if (!current.alarm.equalsIgnoreCase("-1"))
            startAlarm(current.alarm);
        if (!current.notification.equalsIgnoreCase("-1"))
            sendNotification(current.notification);
        if (!current.phonecall.equalsIgnoreCase("-1"))
            startCall(current.phonecall);
        if (!current.sms.equalsIgnoreCase("-1"))
            sendMessage(current.sms);


    }

    private void sendMessage(String sms) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms,null,"Hi",null,null);
    }

    public void sendNotification(String message) {

        if(notificationActive)return;

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle("My notification")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
        notificationActive = true;
    }

    public void startAlarm(String message) {

        if(alarmActive)return;
        Log.v(TAG,"Starting alarm");
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.v(TAG,"Started alarm");
        alarmActive = true;
        startSound();

    }

    private void startSound() {

        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        Log.v(TAG,"starting sound for alarm");
        player.start();
    }


    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v(TAG,"Stopping AlarmSound");
            if(player!=null) {
                player.stop();
                player.reset();
                player.release();
                alarmActive = false;
            }
        }
    };

    private BroadcastReceiver insertRuleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v(TAG,"InsertReceiver");
            ArrayList<Rules> temp = dataBaseAdapter.getAllData();
            Log.v(TAG,"tempSize = "+temp.size());
            Log.v(TAG,"original Queue.SIZE = " + queue.size()+"");
            Log.v(TAG,"original HashMap.SIZE = " + hashMap.size()+"");
            for (Rules rule : temp){
                Log.v(TAG,"looping"+rule.id);
                boolean found1 =  false;
                boolean found2 = false;
                for (Map.Entry<String,Rules> entry : hashMap.entrySet()){

                    if (entry.getValue().id == rule.id) {
                        found1 = true;
                        break;
                    }
                }
                for (Rules rules : queue){
                    if(rules.id == rule.id){
                        found2 = true;
                        break;
                    }
                }
                if (found1 || found2){
                    Log.v(TAG,""+found1+found2);
                }else
                    queue.add(rule);
            }
            Log.v(TAG,"Queue.SIZE = " + queue.size()+"");
            Log.v(TAG,"HashMap.SIZE = " + hashMap.size()+"");
        }
    };

    private BroadcastReceiver deleteRuleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v(TAG,"DeleteReceiver");
            ArrayList<Rules> tempList = dataBaseAdapter.getAllData();
            Log.v(TAG,"tempList Size = "+ tempList.size() );
            for (Rules rule : queue)
            {
                int found =0;
                for (int i=0;i<tempList.size();i++)
                {
                    if (tempList.get(i).id==rule.id)
                    {
                        found=1;
                        break;
                    }
                }
                if (found==0){

                    //Log.v(TAG,"removing databaseID = "+tempList.get(i).id+" QUEUEID = "+rule.id+"index" + i);
                    queue.remove(rule);
                    Log.v(TAG,"Queue.SIZE = " + queue.size()+"");
                    Log.v(TAG,"HashMap.SIZE = " + hashMap.size()+"");
                    return;
                }

            }
            for (Map.Entry<String,Rules> entry : hashMap.entrySet())
            {

                int found = 0;
                for (int i=0;i<tempList.size();i++){
                    if (entry.getValue().id == tempList.get(i).id)
                    {
                        found =1;
                        break;
                    }
                }
                if (found==0) {
                    hashMap.remove(entry);
                    Log.v(TAG, "Queue.SIZE = " + queue.size() + "");
                    Log.v(TAG, "HashMap.SIZE = " + hashMap.size() + "");
                    return;
                }
            }


        }
    };

    private BroadcastReceiver updateRuleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v(TAG,"UpdateRuleReceiver");
            ArrayList<Rules> temp = dataBaseAdapter.getAllData();
            for (Rules newrule : temp){

                for (Map.Entry<String,Rules> entry : hashMap.entrySet()){

                    if (entry.getValue().id == newrule.id) {
                        //String key = entry.getKey();
                        hashMap.remove(entry.getKey());
                        Log.v(TAG,"ReceivedUpdateBroadcastInServiceInHashMap");
                        Log.v(TAG,newrule.state);
                        if(newrule.state.equalsIgnoreCase("active"))
                            queue.add(newrule);
                        Log.v(TAG,"Queue.SIZE = " + queue.size()+"");
                        Log.v(TAG,"HashMap.SIZE = " + hashMap.size()+"");
                        return;
                    }
                }
                for (Rules oldrule : queue){

                    if (oldrule.id == newrule.id){
                        queue.remove(oldrule);
                        Log.v(TAG,"ReceivedUpdateBroadcastInServiceInQueue");
                        Log.v(TAG,newrule.state);
                        if(newrule.state.equalsIgnoreCase("active"))
                            queue.add(newrule);
                        Log.v(TAG,"Queue.SIZE = " + queue.size()+"");
                        Log.v(TAG,"HashMap.SIZE = " + hashMap.size()+"");
                        return;
                    }

                }
            }
            int ID = intent.getIntExtra("id",-1);
            for(Rules newrule :temp){
                if(newrule.id == ID){
                    queue.add(newrule);
                    return;
                }
            }
        }
    };



    public void startCall(String phoneNumber) {

        if(phonecallActive)return;

        Log.v(TAG,"startingCall");

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        //phonecallActive = true;
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
        if(flag==1){
            muteDevice();
        } else {
            unMuteDevice();
        }
    }

    public void startMusic(int flag) {

        if(musicActive)return;

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
                startActivity(launchIntent);
//                startActivityForResult(launchIntent,201);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                playMusiconApp(finalDefaultMusicAppPackage);
                //musicActive = true;
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
            //Log.v(TAG,level+"");
            sysytemCurrentBattery = level;
            enableRule("BATTERY");

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

//        Log.v(TAG,"LOCATION CHANGED"+location.getLatitude()+","+location.getLongitude());
        systemCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        enableRule("LOCATION");
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
                10,
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
            int confidence = Integer.parseInt(intent.getStringExtra("percentage"));

            if(confidence >80)
            {
                activity_type = getType(Integer.parseInt(intent.getStringExtra("activity")));
                enableRule("ACTIVITY");
                Log.v(TAG,activity_type);
            }

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
                return "Idle";
            case DetectedActivity.UNKNOWN:
                return "Unknown";
            case DetectedActivity.WALKING:
                return "Walking";


        }
        return "";

    }

    public void muteDevice() {

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        Log.v(TAG, "Inside MuteDevice");

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dndMode = Settings.Global.getInt(getContentResolver(), "zen_mode");
            }else {
                dndMode = 0;
            }

            Log.v(TAG,"DND mode mute " + dndMode);



        } catch (Exception e) {
            e.printStackTrace();
            dndMode = 99;
        }

        if (dndMode != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.v(TAG,"Android N");

            if (dndMode == 99)
                dndMode = 0;

            return;
        }

        try {

            ringerMode = audioManager.getRingerMode();
            ringerVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            if(dndMode==99)
                dndMode = 0;

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void unMuteDevice() {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        Log.v(TAG, "Inside unMuteDevice");
//        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Log.v(TAG,"DND mode umute " + dndMode);

        try {
            if (dndMode !=0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                Log.v(TAG,"Android N");
                return;

            }

            if (dndMode != 0) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Log.v(TAG, "ringer mode" + audioManager.getRingerMode());
                audioManager.setStreamVolume(AudioManager.STREAM_RING, ringerVolume, AudioManager.FLAG_VIBRATE);
                return;
            }

            audioManager.setRingerMode(ringerMode);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, ringerVolume, AudioManager.FLAG_VIBRATE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
