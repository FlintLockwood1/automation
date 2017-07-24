package com.example.axysu.automate6;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.Fragments.EventFragment;
import com.example.axysu.automate6.Fragments.SaveDialogueFragment;
import com.example.axysu.automate6.Fragments.SaveFragment;
import com.example.axysu.automate6.Fragments.TriggerFragment;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Objects.Rules;

public class AddActivity extends AppCompatActivity implements CustomDialogInterface{

    //FloatingActionButton floatingActionButton;
    Intent intent;
    Intent intent1;
    int batteryStatus;
    String activityStatus;
    Bundle bundle ;
    int id;
    Rules rules;
    private static String TAG ="AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rules = new Rules();
        setAnimation();
        setContentView(R.layout.activity_add);
        handleCallingIntent();
        initializeAndLoadTriggerFragment();
        handleFAB();
        initializeTriggerValue();

    }

    private void initializeTriggerValue() {

        if (id==-1){
            rules.battery = -10;
            rules.mobileData = -1;
            rules.airplaneMode = -1;
            rules.notification = "DEFAULT";
            rules.time = "DEFAULT";
            rules.activity = "DEFAULT";
            rules.alarm = "DEFAULT";
            rules.date = "DEFAULT";
            rules.location = "DEFAULT";
            rules.music = -1;
            rules.silent = -1;
            rules.phonecall = "DEFAULT";
            rules.wifi = -1;
            rules.state="active";
        }
        else {
            rules = getRuleFromDataBasebyID(id);
        }
    }


    private void setAnimation() {

        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setEnterTransition(fade);

    }



    public void handleFAB() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventFragment eventFragment = new EventFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(fragmentManager.findFragmentByTag("EventFragment")==null )
                {
                    transaction.remove(fragmentManager.findFragmentByTag("TriggerFragment"));
                    eventFragment.setArguments(bundle);
                    transaction.add(R.id.addActivityRootLayout, eventFragment, "EventFragment");
                    transaction.addToBackStack("forEvent");
                }
                if (fragmentManager.findFragmentByTag("EventFragment")!=null)
                {
                    SaveDialogueFragment activityAlert5 = new SaveDialogueFragment();
                    activityAlert5.show(getSupportFragmentManager(),"DateAlert");
                }
                transaction.commit();
            }
        });

    }


    public void handleCallingIntent(){

        intent = getIntent();

        bundle = new Bundle();
        if (intent.getExtras()!=null )
        {
            id = intent.getExtras().getInt("id");
            bundle.putInt("id",id);
            Toast.makeText(this,id +"", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeAndLoadTriggerFragment(){
        TriggerFragment triggerFragment = new TriggerFragment();
        triggerFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.addActivityRootLayout, triggerFragment, "TriggerFragment");

        transaction.commit();
    }

    @Override
    public void okButtonClicked(String value, String whichFragment) {

        switch (whichFragment){
            case "BATTERY":
            {
                if (value!=null)
                {
                    this.rules.battery = Integer.parseInt(value);
                }
                Toast.makeText(this,""+rules.battery, Toast.LENGTH_SHORT).show();
                break;
            }
            case "TIME":
            {
                if (value!=null)
                {
                    this.rules.time = (value);
                }
                Toast.makeText(this,rules.time, Toast.LENGTH_SHORT).show();
                break;
            }
            case "LOCATION": {
                if (value != null) {
                    this.rules.location = value;
                }
                Toast.makeText(this,rules.location, Toast.LENGTH_SHORT).show();
                break;
            }
            case "ACTIVITY":
            {
                if (value!=null)
                {
                    this.rules.activity = (value);
                }
                Toast.makeText(this,rules.activity, Toast.LENGTH_SHORT).show();
                break;

            }
            case "DATE":
            {

                if (value!=null)
                {
                    this.rules.date = (value);
                }
                Toast.makeText(this,rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            case "AIRPLANEMODE":
            {
                if (value!=null){
                        rules.airplaneMode = Integer.parseInt(value);
                }
            }
            case "MUSIC":
            {
                if (value!=null)
                {
                        rules.music = Integer.parseInt(value);
                }
                Toast.makeText(this,rules.music+"", Toast.LENGTH_SHORT).show();
                break;
            }
            case "MOBILEDATA":
            {
                if (value!=null)
                {
                        rules.mobileData = Integer.parseInt(value);
                }
                Toast.makeText(this,rules.mobileData+"", Toast.LENGTH_SHORT).show();
                break;
            }
            case "WIFI":
            {
                if (value!=null)
                {
                        rules.wifi  = Integer.parseInt(value);
                }
                Toast.makeText(this,rules.wifi+"", Toast.LENGTH_SHORT).show();
                break;

            }
            case "SILENT":
            {
                if (value!=null)
                {
                        rules.silent = Integer.parseInt(value);
                }
                Toast.makeText(this,rules.silent+"", Toast.LENGTH_SHORT).show();
                break;

            }
            case "ALARM":
            {

                if (value!=null)
                {
                    this.rules.alarm = (value);
                }
                Toast.makeText(this,rules.alarm+"", Toast.LENGTH_SHORT).show();
                break;
            }
            case "NOTIFICATION":
            {

                if (value!=null)
                {
                    this.rules.notification = (value);
                }
                Toast.makeText(this,rules.notification+"", Toast.LENGTH_SHORT).show();
                break;
            }
            case "PHONECALL":
            {

                if (value!=null)
                {
                    this.rules.phonecall = (value);
                }
                Toast.makeText(this,rules.phonecall+"", Toast.LENGTH_SHORT).show();
                break;
            }
            case "SAVE":
            {
                if (value.equals("nameyourrule")) {
                    value="Rule";
                }
                    rules.name=value;

                    long success = 0;
                    long rowsUpdated = 0;
                    if (id == -1) {
                        success = new DataBaseAdapter(this).insertRule(this.rules);
                        if (success < 0) {
                            Log.v("AddActivity", "Success");
                            Toast.makeText(this, "Failed to INsert Data", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Successfully INserted Data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        rowsUpdated = new DataBaseAdapter(this).updateTable(id,rules);
                        Toast.makeText(this, ""+rowsUpdated, Toast.LENGTH_SHORT).show();
                    }



                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;

        }

    }

    private Rules getRuleFromDataBasebyID(int id) {
        Log.v(TAG,"id :" +id);
        return (new DataBaseAdapter(this).getDataByIndex(id)).get(0);

    }
}
