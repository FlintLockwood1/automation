package com.example.axysu.automate6.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by axysu on 7/17/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String createMaster = "CREATE TABLE RULE " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "location VARCHAR(255)," +
            "battery INTEGER," +
            "time VARCHAR(255), " +
            "date VARCHAR(255)," +
            "activity VARCHAR(255))";
    private static final String createEvent = "CREATE TABLE EVENT " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name VARCHAR(255))";
    private static final String createTrigger = "CREATE TABLE TRIGGER " +
            "(ruleid integer, " +
            "eventid integer)";

    private static final String DATABASE_NAME = "myDatabase";
    private static final int VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createMaster);
        db.execSQL(createEvent);
        db.execSQL(createTrigger);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS RULE");
        db.execSQL("DROP TABLE IF EXISTS EVENT");
        db.execSQL("DROP TABLE IF EXISTS TRIGGER");
        onCreate(db);

    }

    public void insertDataIntoEventTable(){


    }

    public void insertDataIntoMasterTable(){


    }

    public void insertDataIntoTriggerTable(){


    }


}
