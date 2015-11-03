package com.sinduran.heartrate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sinduran.androidrecord.HistoryActivity;

/**
 * Created by rick on 02/11/2015.
 */
public class HeartRateMainActivity extends Activity {
    private static final String DEBUG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alt);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void calculateHeartRate(View view){
        //Need async task
        Log.d(DEBUG, "TODO");
    }
}
