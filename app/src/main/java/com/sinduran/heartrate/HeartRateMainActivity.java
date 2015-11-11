package com.sinduran.heartrate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sinduran.androidrecord.HistoryActivity;

import java.util.Random;

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

        private int heartrate = 60;
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
            this.heartrate = (int) genRandom(40,120);
            return Long.valueOf(1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            double rate = Math.random();
            heartRateTV.setText(heartrate + " bpm");

        }

        private double genRandom(double min, double max) {
            Random r = new Random();
            return (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
        }
    }
}
