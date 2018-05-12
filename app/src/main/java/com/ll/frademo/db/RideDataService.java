package com.ll.frademo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ll.frademo.entity.Locate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by l4396 on 2018/4/11.
 */

public class RideDataService {
    Context context;

    public RideDataService(Context context){
        this.context = context;
    }
    public void saveObject(List<Locate> locates){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(locates);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();

            Dbhelper dbhelper = Dbhelper.getInstance(context);
            SQLiteDatabase database = dbhelper.getWritableDatabase();

            String countQuery = "SELECT * FROM " + "rideData";

            Cursor cursor = database.rawQuery(countQuery,null);
            int cnt = cursor.getCount();
            cursor.close();

            database.execSQL("insert into rideData values(null,?,?)",new Object[]{data,cnt});
            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<List<Locate>> getObject(){
        ArrayList<List<Locate>> list = new ArrayList<List<Locate>>();

        List<Locate> locates = null;
        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from rideData",null);
        if (cursor != null){
            while (cursor.moveToNext()){
                byte data[] = cursor.getBlob(cursor.getColumnIndex("ride_data"));
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
                    locates = (List<Locate>) inputStream.readObject();
                    inputStream.close();
                    byteArrayInputStream.close();

                    list.add(locates);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public List<Locate> getObject(int position) {

        List<Locate> locates = null;
        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from rideData", null);

        if (cursor != null) {

            cursor.moveToPosition(position);

            byte data[] = cursor.getBlob(cursor.getColumnIndex("ride_data"));
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
            try {
                ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                locates = (List<Locate>) inputStream.readObject();
                inputStream.close();
                arrayInputStream.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
            cursor.close();


        }
        return locates;

    }

    public void deletData(int num){
        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        database.delete("rideData","num=?",new String[]{num+""});
        database.close();
    }


}
