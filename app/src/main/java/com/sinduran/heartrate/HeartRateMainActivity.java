package com.sinduran.heartrate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sinduran.androidrecord.HistoryActivity;

import java.util.Random;

public class HeartRateMainActivity extends Activity {
    private static final String DEBUG = "DEBUG";
    private TextView heartRateTV;
    private HeartRateDatabaseAdapter heartRateDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alt);
        heartRateTV = (TextView) findViewById(R.id.heartRateTV);
        heartRateDatabaseAdapter = new HeartRateDatabaseAdapter(this);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void calculateHeartRate(View view) {
        //Need async task
        new UpdateHRTask().execute();
    }

    private class UpdateHRTask extends AsyncTask<String, Integer, Long> {

        HeartRate heartrate;

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
            this.heartrate = new HeartRate(genRandomHeartRate());
            long id = heartRateDatabaseAdapter.insertData(heartrate.getHeartRate(),heartrate.getTime());

            if (id < 0){
                Log.e("HRDatabase", "Unsuccessful in insertion");
            } else {
                Log.d("HRDatabase", "Successfully Inserted A Row");
            }

            return Long.valueOf(1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            heartRateTV.setText(heartrate.getHeartRate() + " BPM");

        }

        private int genRandomHeartRate() {
            Random r = new Random();
            return r.nextInt((220 - 40) + 1) + 40;
        }
    }
}
