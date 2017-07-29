package com.example.axysu.automate6;

import android.app.Application;
import android.content.Context;

/**
 * Created by ashish on 25-07-2017.
 */

public class ApplicationClass extends Application {

    private static Context context;

    public static Context getAppContext() {

        return ApplicationClass.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context= getApplicationContext();
    }
}
