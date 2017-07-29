package com.example.axysu.automate6.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.axysu.automate6.Services.MyService;

/**
 * Created by axysu on 7/18/2017.
 */

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent pushIntent = new Intent(context, MyService.class);
        context.startService(pushIntent);
    }
}
