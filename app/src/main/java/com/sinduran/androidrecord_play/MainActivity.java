package com.sinduran.androidrecord_play;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private final int source = MediaRecorder.AudioSource.MIC;
    private final int output_format = MediaRecorder.OutputFormat.THREE_GPP;
    private final int encoder = MediaRecorder.AudioEncoder.AAC;
    private final String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                            +"/myaudio.3gpp";;

    private ToggleButton playButton;
    private ToggleButton recordButton;


    private boolean isRecording = false;

    @Override
    protected void onStart() {
        super.onStart();
        recordButton = (ToggleButton) findViewById(R.id.recordButton);
        playButton = (ToggleButton) findViewById(R.id.playButton);

        if (!hasMicrophone())
        {
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
        }

        Log.d(TAG,audioFilePath);
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
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlayClick(View view){
        ToggleButton toggleButton = (ToggleButton) view;
        if (toggleButton.isChecked()) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        recordButton.setEnabled(false);
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            mp.release();
                            mediaPlayer = null;
                            playButton.setChecked(false);
                        /*if audio finishes before user presses on stop
                        * then automatically release resources*/
                        }
                    });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void startRecording(){
        isRecording = true;
        playButton.setEnabled(false);

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(source);
            mediaRecorder.setOutputFormat(output_format);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(encoder);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        recordButton.setEnabled(true);
    }

    private void stopRecording() {
        if(isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            playButton.setEnabled(true);
        }
    }

    private boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
}