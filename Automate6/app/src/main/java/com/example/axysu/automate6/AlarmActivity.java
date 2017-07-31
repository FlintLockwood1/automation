package com.example.axysu.automate6;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    public void end(String state) {
        Intent data = new Intent("ALARMSTOPPED");
        data.putExtra("state",state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(data);
        finish();
    }

    public void snooze(View view) {
        end("snooze");
    }

    public void dissmiss(View view) {
        end("dismiss");
    }

    @Override
    public void onBackPressed() {

    }


}
