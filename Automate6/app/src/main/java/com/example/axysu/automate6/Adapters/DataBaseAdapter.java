package com.example.axysu.automate6.Adapters;

/**
 * Created by axysu on 7/18/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.axysu.automate6.Objects.Rules;

public class DataBaseAdapter {

    MyDbHelper helper;

    public void DataBaseAdapter(Context context) {

        helper = new MyDbHelper(context);
    }


    public long insertRule(Rules rule) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(helper.MTIME, rule.time);
        values1.put(helper.MDATE, rule.date);
        values1.put(helper.MLOCATION, rule.location);
        values1.put(helper.MACTIVITY, rule.activity);
        values1.put(helper.MBATTERY, rule.battery);
        values1.put(helper.MNAME, rule.name);
        values1.put(helper.AIRPLANEMODE,rule.airplaneMode);
        values1.put(helper.WIFI,rule.wifi);
        values1.put(helper.MOBILEDATA,rule.mobileData);
        values1.put(helper.SILENT,rule.silent);
        values1.put(helper.ALARM,rule.alarm);
        values1.put(helper.NOTIFICATION,rule.notification);
        values1.put(helper.PHONECALL,rule.phonecall);
        values1.put(helper.MUSIC,rule.music);
        long id = db.insert("RULE", null, values1);
        return id;

    }

    public void getAllData(){

        SQLiteDatabase db = helper.getWritableDatabase();
        String columns[] = {MyDbHelper.MUID};
    }


    static class MyDbHelper extends SQLiteOpenHelper {

        private static final String MUID = "_id";
        private static final String MTIME = "time";
        private static final String MDATE = "date";
        private static final String MACTIVITY = "activity";
        private static final String MLOCATION = "location";
        private static final String MBATTERY = "battery";
        private static final String MNAME = "name";
        private static final String AIRPLANEMODE = "airplanemode"; //1
        private static final String WIFI = "wifi";         //2
        private static final String MOBILEDATA = "mobiledata";   //3
        private static final String SILENT = "silent";       //4
        private static final String ALARM = "alarm";        //5
        private static final String ALARMMESSAGE = "alarmmessage";
        private static final String NOTIFICATION = "notification";
        private static final String PHONECALL = "phonecall";//6
        private static final String MUSIC = "music";

        Context context;



        private static final String CREATEMASTER = "CREATE TABLE RULE (" +
                MUID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MNAME + "VARCHAR(255)," +
                MLOCATION + "VARCHAR(255)," +
                MBATTERY + "INTEGER," +
                MTIME + " VARCHAR(255), " +
                MDATE + " VARCHAR(255)," +
                MACTIVITY + "VARCHAR(255)" +
                AIRPLANEMODE + "VARCHAR(255)"+
                WIFI + "VARCHAR(255)"+
                MOBILEDATA + "VARCHAR(255)" +
                SILENT + "VARCHAR(255)" +
                ALARM + "VARCHAR(255)" +
                ALARMMESSAGE + "VARCHAR(255)" +
                NOTIFICATION + "VARCHAR(255)" +
                PHONECALL + "VARCHAR(255)" +
                MUSIC + "VARCHAR(255))";

        private static final String DATABASE_NAME = "myDatabase";
        private static final int DATABASE_VERSION = 1;


        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATEMASTER);
            } catch (SQLException e) {
                Toast.makeText(context,""+e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {

            try {
                db.execSQL("DROP TABLE IF EXISTS RULE");
            } catch (SQLException e) {
                Toast.makeText(context,""+e, Toast.LENGTH_SHORT).show();
            }
            onCreate(db);

        }

    }
}