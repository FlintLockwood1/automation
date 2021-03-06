package com.example.axysu.automate6.Adapters;

/**
 * Created by axysu on 7/18/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.axysu.automate6.Objects.Rules;

import java.util.ArrayList;

public class DataBaseAdapter {

    private MyDbHelper helper;
    private  static String TAG ="DataBaseAdapter";
    private Context context;

    public  DataBaseAdapter(Context context) {

        helper = new MyDbHelper(context);
        this.context = context;
    }


    public long insertRule(Rules rule) {

        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert("RULE", null, generateAndPopulateContentValues(rule));
        Intent intent = new Intent("INSERTEDROW");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        return id;

    }

    public ArrayList<Rules> getAllData(){


        SQLiteDatabase db = helper.getWritableDatabase();

        String columns[] = {MyDbHelper.MUID,MyDbHelper.MTIME,MyDbHelper.MDATE,MyDbHelper.MACTIVITY
        ,MyDbHelper.MLOCATION,MyDbHelper.MBATTERY,MyDbHelper.MNAME,MyDbHelper.AIRPLANEMODE,MyDbHelper.WIFI
        ,MyDbHelper.MOBILEDATA,MyDbHelper.SILENT,MyDbHelper.ALARM,MyDbHelper.NOTIFICATION,MyDbHelper.PHONECALL
        ,MyDbHelper.SMS,MyDbHelper.MUSIC,MyDbHelper.STATE};

        Cursor cursor = db.query("RULE",columns,null,null,null,null,null);
        return handleCursor(cursor);
    }

    public ArrayList<Rules> getDataByIndex(int index){

        SQLiteDatabase db = helper.getWritableDatabase();
        String selectionArgs [] = new String[]{String.valueOf(index)};

        String columns[] = {MyDbHelper.MUID,MyDbHelper.MTIME,MyDbHelper.MDATE,MyDbHelper.MACTIVITY
                ,MyDbHelper.MLOCATION,MyDbHelper.MBATTERY,MyDbHelper.MNAME,MyDbHelper.AIRPLANEMODE,MyDbHelper.WIFI
                ,MyDbHelper.MOBILEDATA,MyDbHelper.SILENT,MyDbHelper.ALARM,MyDbHelper.NOTIFICATION,MyDbHelper.PHONECALL
                ,MyDbHelper.SMS,MyDbHelper.MUSIC,MyDbHelper.STATE};

      //  Cursor cursor = db.query("RULE",columns,MyDbHelper.MUID + " =?",selectionArgs,null,null,null);
        String query = "SELECT * FROM RULE WHERE " + MyDbHelper.MUID + " = "+ index;
        Log.v(TAG,"quey :" +query);
        Cursor cursor1 = db.rawQuery(query,null);
        return handleCursor(cursor1);

    }


    public int updateTable(int id,Rules newrules){

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereClause = {String.valueOf(id)};
        int rowsUpdated = db.update("RULE",generateAndPopulateContentValues(newrules),MyDbHelper.MUID + " =?",whereClause);
        Intent intent = new Intent("UPDATEDROW");
        intent.putExtra("id",id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        return rowsUpdated;
    }



    public int delete(int id){

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereClause = new String[]{String.valueOf(id)};
        int rowsDeleted = db.delete("RULE",MyDbHelper.MUID + " = ?",whereClause);
        Intent intent = new Intent("DELETEDROW");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        return rowsDeleted;
    }

    public ContentValues generateAndPopulateContentValues(Rules rule){

        ContentValues values1 = new ContentValues();

        values1.put(helper.MTIME, rule.time);
        values1.put(helper.MDATE, rule.date);
        values1.put(helper.MLOCATION, rule.location);
        values1.put(helper.MACTIVITY, rule.activity);
        values1.put(helper.MBATTERY, rule.battery);
        values1.put(helper.MNAME, rule.name);
        values1.put(helper.STATE,rule.state);
        values1.put(helper.AIRPLANEMODE, rule.airplaneMode);
        values1.put(helper.MOBILEDATA, rule.mobileData);
        values1.put(helper.SILENT, rule.silent);
        values1.put(helper.MUSIC, rule.music);
        values1.put(helper.ALARM,rule.alarm);
        values1.put(helper.NOTIFICATION,rule.notification);
        values1.put(helper.PHONECALL,rule.phonecall);
        values1.put(helper.SMS,rule.sms);
        values1.put(helper.WIFI,rule.wifi);

        return  values1;
    }

    public ArrayList<Rules> handleCursor(Cursor cursor){
        //Log.v(TAG,"outside cursor");

        ArrayList<Rules> arrayList = new ArrayList<>();
        while(cursor.moveToNext()){
            //Log.v(TAG,"inside cursor");
            Rules rules = new Rules();
            rules.id = cursor.getInt(cursor.getColumnIndex(MyDbHelper.MUID));

            rules.time = cursor.getString(cursor.getColumnIndex(MyDbHelper.MTIME));
            rules.date = cursor.getString(cursor.getColumnIndex(MyDbHelper.MDATE));
            rules.activity = cursor.getString(cursor.getColumnIndex(MyDbHelper.MACTIVITY));
            rules.location = cursor.getString(cursor.getColumnIndex(MyDbHelper.MLOCATION));
            rules.battery = cursor.getInt(cursor.getColumnIndex(MyDbHelper.MBATTERY));
            rules.name = cursor.getString(cursor.getColumnIndex(MyDbHelper.MNAME));

            rules.alarm = cursor.getString(cursor.getColumnIndex(MyDbHelper.ALARM));
            rules.notification = cursor.getString(cursor.getColumnIndex(MyDbHelper.NOTIFICATION));
            rules.phonecall = cursor.getString(cursor.getColumnIndex(MyDbHelper.PHONECALL));
            rules.sms = cursor.getString(cursor.getColumnIndex(MyDbHelper.SMS));

            rules.mobileData = (cursor.getInt(cursor.getColumnIndex(MyDbHelper.MOBILEDATA)));
            rules.airplaneMode = (cursor.getInt(cursor.getColumnIndex(MyDbHelper.AIRPLANEMODE)));
            rules.silent = (cursor.getInt(cursor.getColumnIndex(MyDbHelper.SILENT)));
            rules.music = (cursor.getInt(cursor.getColumnIndex(MyDbHelper.MUSIC)));
            rules.wifi = (cursor.getInt(cursor.getColumnIndex(MyDbHelper.WIFI)));
            rules.state=(cursor.getString(cursor.getColumnIndex(MyDbHelper.STATE)));

            arrayList.add(rules);

        }
        cursor.close();
        return arrayList;
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
        private static final String ALARM = "alarm";
        private static final String NOTIFICATION = "notification";
        private static final String PHONECALL = "phonecall";//6
        private static final String SMS = "sms";
        private static final String MUSIC = "music";
        private static final String STATE = "state";

        Context context;



        private static final String CREATEMASTER = "CREATE TABLE RULE ( " +
                MUID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MNAME + " VARCHAR(255)," +
                MLOCATION + " VARCHAR(255)," +
                MBATTERY + " INTEGER," +
                MTIME + " VARCHAR(255), " +
                MDATE + " VARCHAR(255)," +
                MACTIVITY + " VARCHAR(255)," +
                AIRPLANEMODE + " VARCHAR(255),"+
                WIFI + " VARCHAR(255),"+
                MOBILEDATA + " VARCHAR(255)," +
                SILENT + " VARCHAR(255)," +
                ALARM + " VARCHAR(255)," +
                NOTIFICATION + " VARCHAR(255)," +
                PHONECALL + " VARCHAR(255)," +
                SMS + " VARCHAR(255)," +
                MUSIC + " VARCHAR(255)," +
                STATE + " VARCHAR(255))";

        private static final String DATABASE_NAME = "myDatabase";
        private static final int DATABASE_VERSION = 6002;


        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATEMASTER);
            } catch (SQLException e) {
                e.printStackTrace();
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