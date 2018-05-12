package com.ll.frademo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by l4396 on 2018/4/11.
 */

public class Dbhelper extends SQLiteOpenHelper {

    private static Dbhelper dbhelper = null;

    public static Dbhelper getInstance(Context context){
        if (dbhelper == null){
            dbhelper = new Dbhelper(context);
        }
        return dbhelper;
    }
    private Dbhelper(Context context){
        super(context,"datebase.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_class_table="create table if not exists rideData(" + "_id integer primary key autoincrement,"+"ride_data text,num integer)";
        db.execSQL(sql_class_table);
        sql_class_table="create table if not exists sportData(" + "_id integer primary key autoincrement,"+"sport_data text,num integer)";
        db.execSQL(sql_class_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
