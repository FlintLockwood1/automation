package com.example.axysu.automate6;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.Adapters.RulesActivityPagerAdapter;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Services.MyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CustomDialogInterface{

    Toolbar toolbar;
    DrawerLayout drawer;
    TabLayout tabLayout;
    ViewPager viewPager;
    RulesActivityPagerAdapter myAdapter;
    Intent intent;
    public static int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private static String TAG ="MainActivity";
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setAnimation();
        setContentView(R.layout.activity_main);
        handleToolBar();
        handleFAB();
        handleDrawer();
        handlePager();
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);
        FetchDataForRulesLists.data= dataBaseAdapter.getAllData();
        FetchDataForRulesLists.activedata = new ArrayList<>();
        FetchDataForRulesLists.inactivedata = new ArrayList<>();
        avatar = (ImageView) findViewById(R.id.avatar);
        for (int i=0;i<FetchDataForRulesLists.data.size();i++){

            if (FetchDataForRulesLists.data.get(i).state.equalsIgnoreCase("Active"))
                FetchDataForRulesLists.activedata.add(FetchDataForRulesLists.data.get(i));
            else
                FetchDataForRulesLists.inactivedata.add(FetchDataForRulesLists.data.get(i));
        }
        checkAndRequestPermissions();

        IntentFilter filter = new IntentFilter("com.journaldev.CUSTOM_INTENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver,filter);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG,"notifyDataSetChanged");
            if(myAdapter!=null)
                myAdapter.notifyDataSetChanged();
        }
    };

    private void setAnimation() {

        TransitionInflater inflater = TransitionInflater.from(this);
        Transition transition = inflater.inflateTransition(R.transition.transition_from_main_to_add);
        getWindow().setExitTransition(transition);
        getWindow().setEnterTransition(transition);
    }

    public void handlePager(){

        myAdapter = new RulesActivityPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(myAdapter);
        tabLayout.setTabsFromPagerAdapter(myAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void handleFAB(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = -1;
                exitMainActivity(id);
            }
        });

    }

    public void handleToolBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void handleDrawer(){

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

            Intent intent = new Intent(this,AlarmActivity.class);
            startActivityForResult(intent,0);

            //DAsh

        } else if (id == R.id.nav_manage) {

            // Settings

        }else if (id==R.id.nav_rate){

            Toast.makeText(this, "FiNiSh ThE ApP FiRsT", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                if (data.getStringExtra("state")!=null)
//                    Toast.makeText(this, data.getStringExtra("state"), Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void exitMainActivity(int id){

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);

        Intent intent = new Intent(this,AddActivity.class);
        intent.putExtra("id",id);

        startActivity(intent,compat.toBundle());
    }

    @Override
    public void okButtonClicked(String value, String whichFragment) {


    }

    public void startService(){
            final Intent intent = new Intent(MainActivity.this, MyService.class);
            startService(intent);

    }


    private  boolean checkAndRequestPermissions() {
        Log.v(TAG,"checkAndRequestPermissions");
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionReadMessage = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
        int permissionRecieveMessage = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permisionReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permisionWriteContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        int permisionReadCallLog = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int permisionWriteCallLog = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG);
        int permisionCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int permisionPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        HashMap<String, Integer> permissionMap = new HashMap<>();
        permissionMap.put(Manifest.permission.SEND_SMS, permissionSendMessage);
        permissionMap.put(Manifest.permission.READ_SMS, permissionReadMessage);
        permissionMap.put(Manifest.permission.RECEIVE_SMS,permissionRecieveMessage);
        permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION,locationPermission);
        permissionMap.put(Manifest.permission.READ_CONTACTS,permisionReadContacts);
        permissionMap.put(Manifest.permission.WRITE_CONTACTS,permisionWriteContacts);
        permissionMap.put(Manifest.permission.READ_CALL_LOG,permisionReadCallLog);
        permissionMap.put(Manifest.permission.WRITE_CALL_LOG,permisionWriteCallLog);
        permissionMap.put(Manifest.permission.CALL_PHONE,permisionCallPhone);
        permissionMap.put(Manifest.permission.READ_PHONE_STATE,permisionPhoneState);


        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permisionReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permisionWriteContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (permisionReadCallLog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }
        if (permisionWriteCallLog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CALL_LOG);
        }
        if (permisionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permisionPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionReadMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionRecieveMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }else {
            startService();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){
            Log.v(TAG,"mulitple request");

            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_CALL_LOG, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_CALL_LOG, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {

                for (int i = 0; i < permissions.length; i++){
                    perms.put(permissions[i], grantResults[i]);
                    Log.v(TAG,"permision "+permissions[i] +" = " + grantResults[i]);
                }

                // Check for both permissions
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){

                    Log.d(TAG, "all permission granted");
                    startService();

                    // process the normal flow
                    //else any one or both the permissions are not granted
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALL_LOG)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                        showDialogOK("Permission required for this app to work properly",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                .show();
                        //                            //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void promptUser (View view){

        Toast.makeText(this, "Hold to change ur profile pic", Toast.LENGTH_SHORT).show();
    }

}



