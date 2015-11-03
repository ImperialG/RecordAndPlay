package com.sinduran.heartrate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sinduran.androidrecord.HistoryActivity;

/**
 * Created by rick on 02/11/2015.
 */
public class HeartRateMainActivity extends Activity {
    private static final String DEBUG = "DEBUG";
    private TextView heartRateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alt);
        heartRateTV = (TextView) findViewById(R.id.heartRateTV);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void calculateHeartRate(View view) {
        //Need async task
        new UpdateHRTask().execute();
    }

    public class UpdateHRTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected void onPreExecute() {
            heartRateTV.setText("Calculating..");
        }

        @Override
        protected Long doInBackground(String... params) {
            //Sleep to simulate time delay
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Long.valueOf(1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            heartRateTV.setText("121 bpm");
        }
    }
}
