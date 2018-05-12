package com.ll.frademo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ll.frademo.entity.SportData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SportDataService {
    Context context;
    public SportDataService(Context context){
        this.context =context;
    }
    public void saveObject(SportData sportData){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getWritableDatabase();
        String countQuery = "SELECT * FROM " + "sportData";
        Cursor cursor = database.rawQuery(countQuery,null);
        int cnt = cursor.getCount();
        cursor.close();

        sportData.setNum(cnt);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(sportData);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();

            database.execSQL("insert into sportData values(null,?,?)",new Object[]{data,cnt});
            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<SportData> getObject() throws ClassNotFoundException {
        SportData sportData = null;
        ArrayList<SportData> sportDatas = new ArrayList<SportData>();

        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from sportData",null);

        if (cursor != null){
            while (cursor.moveToNext()){
                byte data[] = cursor.getBlob(cursor.getColumnIndex("sport_data"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
                    sportData = (SportData) objectInputStream.readObject();
                    objectInputStream.close();
                    arrayInputStream.close();


                    sportDatas.add(sportData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return sportDatas;
    }

    public void deletData(int num) {
        Dbhelper dbhelper = Dbhelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        database.delete("sportData", "num=?", new String[]{num + ""});
        database.close();
    }
}
