package com.sinduran.androidrecord_play;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private final int source = MediaRecorder.AudioSource.MIC;
    private File appDir;
    private final String audioFilePath = "myaudio.wav";
    private Android_Record_Play arp;
    private ToggleButton playButton;
    private ToggleButton recordButton;
    private Chronometer myChronometer;

    @Override
    protected void onStart() {
        super.onStart();
        String appPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ARP/";
        appDir = new File(appPath);
        if(!appDir.exists()){
            appDir.mkdirs();
        }
        recordButton = (ToggleButton) findViewById(R.id.recordButton);
        playButton = (ToggleButton) findViewById(R.id.playButton);
        myChronometer = (Chronometer)findViewById(R.id.chronometer);

        if (!hasMicrophone())
        {
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
        }

        arp = new Android_Record_Play(source,appPath,audioFilePath);
        arp.setOnPlayerCompletion(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setChecked(false);
                stopPlaying();
                /*
                 * if audio finishes before user presses on stop
                 * then automatically release resources
                 */
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRecordClick(View view){
        ToggleButton toggleButton = (ToggleButton) view;
        if (toggleButton.isChecked()) {
            myChronometer.setBase(SystemClock.elapsedRealtime());
            startRecording();
            myChronometer.start();
        } else {
            stopRecording();
        }
    }

    public void onPlayClick(View view){
        ToggleButton toggleButton = (ToggleButton) view;
        if (toggleButton.isChecked()) {
            myChronometer.setBase(SystemClock.elapsedRealtime());
            startPlaying();
            myChronometer.start();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        recordButton.setEnabled(false);
        arp.startPlaying();
    }

    private void startRecording(){
        playButton.setEnabled(false);
        arp.startRecording();
    }

    private void stopPlaying() {
        myChronometer.stop();
        arp.stopPlaying();
        recordButton.setEnabled(true);
    }

    private void stopRecording() {
        myChronometer.stop();
        arp.stopRecording();
        playButton.setEnabled(true);
    }

    private boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
}