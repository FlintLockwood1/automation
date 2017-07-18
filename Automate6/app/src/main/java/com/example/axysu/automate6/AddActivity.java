package com.example.axysu.automate6;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.axysu.automate6.Fragments.EventFragment;
import com.example.axysu.automate6.Fragments.SaveDialogueFragment;
import com.example.axysu.automate6.Fragments.SaveFragment;
import com.example.axysu.automate6.Fragments.TriggerDateDialogueFragment;
import com.example.axysu.automate6.Fragments.TriggerFragment;
import com.example.axysu.automate6.Objects.Rules;

public class AddActivity extends AppCompatActivity {

    //FloatingActionButton floatingActionButton;
    Intent intent;
    Intent intent1;
    int batteryStatus;
    String activityStatus;
    Bundle bundle ;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_add);
        handleCallingIntent();
        initializeAndLoadTriggerFragment();
        handleFAB();
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
            bundle.putInt("battery",id);
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


}
