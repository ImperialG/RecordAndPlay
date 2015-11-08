package com.sinduran.heartrate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeartRateDatabaseAdapter {
    private HeartRateDatabaseHelper heartRateDatabaseHelper;
    private SQLiteDatabase db = heartRateDatabaseHelper.getWritableDatabase();

    public HeartRateDatabaseAdapter(Context context){
        Log.d("HRDatabase", "HeatRateDatabaseAdapter constructor called");
        heartRateDatabaseHelper = new HeartRateDatabaseHelper(context);
    }

    public long insertData(int heartRate, String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(HeartRateDatabaseHelper.HEART_RATE, heartRate);
        contentValues.put(HeartRateDatabaseHelper.DATE, time);
        return db.insert(HeartRateDatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public String[] getAllData(){
        String[] columns = {HeartRateDatabaseHelper.HEART_RATE, HeartRateDatabaseHelper.DATE};
        Cursor c = db.query(HeartRateDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        String[] dataBuffer = new String[c.getCount()];

        int heartRateColumnId = c.getColumnIndex(HeartRateDatabaseHelper.HEART_RATE);
        int dateColumnId = c.getColumnIndex(HeartRateDatabaseHelper.DATE);

        if (c != null){
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                String heartRate = c.getString(heartRateColumnId);
                String date = c.getString(dateColumnId);

                dataBuffer[i] = heartRate + " BPM - " + date;
                c.moveToNext();
            }
            c.close();
        }

        List<String> list = Arrays.asList(dataBuffer);

        Collections.reverse(list);

        dataBuffer = (String[]) list.toArray();

        return dataBuffer;
    }

    public void deleteAll(){
        db.delete(HeartRateDatabaseHelper.TABLE_NAME, null, null);
    }

    public void deleteById(int id){
        db.delete(HeartRateDatabaseHelper.TABLE_NAME, HeartRateDatabaseHelper.UID + " =? " + id, null);
    }

    public void deleteByDate(String date){
        db.delete(HeartRateDatabaseHelper.TABLE_NAME, HeartRateDatabaseHelper.DATE + " =? " + date, null);
    }

    private static class HeartRateDatabaseHelper extends SQLiteOpenHelper{
        private Context context;

        private static final String DATABASE_NAME = "heartrate";
        private static final String TABLE_NAME = "HEARTRATE";
        //If CREATE_TABLE is changed remember to update the version number in order for onUpgrade() to be called
        private static final int DATABASE_VERSION = 1;

        private static final String UID = "_id";
        private static final String HEART_RATE = "HeartRate";
        private static final String DATE = "Date";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HEART_RATE + " INTEGER, " + DATE + " VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public HeartRateDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("HRDatabase", "HeatRateDatabaseHelper constructor called");
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                Log.d("HRDatabase", "onCreate called");
            } catch (SQLException e){
                Log.e("HRDatabase", e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
                Log.d("HRDatabase", "onUpgrade called");
            } catch (SQLException e){
                Log.e("HRDatabase", e.toString());
            }

        }
    }
}
