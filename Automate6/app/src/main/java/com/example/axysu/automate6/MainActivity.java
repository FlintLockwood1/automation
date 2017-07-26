package com.example.axysu.automate6;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.Adapters.RulesActivityPagerAdapter;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Services.MyService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CustomDialogInterface{

    Toolbar toolbar;
    DrawerLayout drawer;
    TabLayout tabLayout;
    ViewPager viewPager;
    RulesActivityPagerAdapter myAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_main);
        handleToolBar();
        handleFAB();
        handleDrawer();
        handlePager();
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);
        FetchDataForRulesLists.data= dataBaseAdapter.getAllData();
        FetchDataForRulesLists.activedata = new ArrayList<>();
        FetchDataForRulesLists.inactivedata = new ArrayList<>();
        for (int i=0;i<FetchDataForRulesLists.data.size();i++){

            if (FetchDataForRulesLists.data.get(i).state.equalsIgnoreCase("Active"))
                FetchDataForRulesLists.activedata.add(FetchDataForRulesLists.data.get(i));
            else
                FetchDataForRulesLists.inactivedata.add(FetchDataForRulesLists.data.get(i));
        }

        startService();
    }

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

}



