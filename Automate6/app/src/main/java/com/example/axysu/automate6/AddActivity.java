package com.example.axysu.automate6;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
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

        if (id==-1 || true){
            rules.battery = 50;
            rules.mobileData = false;
            rules.airplaneMode = false;
            rules.notification = "DEFAULT";
            rules.time = "DEFAULT";
            rules.activity = "DEFAULT";
            rules.alarm = "DEFAULT";
            rules.date = "DEFAULT";
            rules.location = "DEFAULT";
            rules.music = false;
            rules.silent = false;
            rules.phonecall = "DEFAULT";
            rules.wifi = false;
        }
        else {
            getRuleFromDataBasebyID(rules.id);
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
                SaveFragment saveFragment = new SaveFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(fragmentManager.findFragmentByTag("EventFragment")==null )
                {
                    transaction.remove(fragmentManager.findFragmentByTag("TriggerFragment"));
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
            case "SAVE":
            {
                long success = new DataBaseAdapter().insertRule(this.rules);
                if (success<0){
                    Toast.makeText(this, "Failed to INsert Data", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Successfully INserted Data", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;

        }

    }

    private void getRuleFromDataBasebyID(int id) {

    }
}
