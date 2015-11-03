package com.sinduran.androidrecord_play;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private final int source = MediaRecorder.AudioSource.MIC;
    private File appDir;
    private final String audioFilePath = "myaudio.wav";
    private Android_Record_Play arp;
    private ToggleButton playButton;
    private ToggleButton recordButton;
    private Chronometer myChronometer;

    private EditText nameTextField;
    private EditText dobTextField;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private RadioGroup genderButtons;
    private Button saveChangesButton;
    private boolean repeat;
    private TextView nameDisplayField;
    private TextView dobDisplayField;
    private TextView genderDisplayField;


    @Override
    protected void onStart() {
        super.onStart();

        if (repeat){

            displayProfileDetails();
        } else{
            nameTextField = (EditText) findViewById(R.id.nameTextBox);
            dobTextField = (EditText) findViewById(R.id.dobTextBox);
            maleButton = (RadioButton) findViewById(R.id.maleRadioButton);
            femaleButton = (RadioButton) findViewById(R.id.femaleRadioButton);
            genderButtons = (RadioGroup) findViewById(R.id.radioGroup);
            saveChangesButton = (Button) findViewById(R.id.saveButton);
        }
//        String appPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ARP/";
//        appDir = new File(appPath);
//        if(!appDir.exists()){
//            appDir.mkdirs();
//        }
//        recordButton = (ToggleButton) findViewById(R.id.recordButton);
//        playButton = (ToggleButton) findViewById(R.id.playButton);
//        myChronometer = (Chronometer)findViewById(R.id.chronometer);
//
//        if (!hasMicrophone())
//        {
//            playButton.setEnabled(false);
//            recordButton.setEnabled(false);
//        } else {
//            playButton.setEnabled(false);
//        }
//
//        arp = new Android_Record_Play(source,appPath,audioFilePath);
//        arp.setOnPlayerCompletion(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                playButton.setChecked(false);
//                stopPlaying();
//                /*
//                 * if audio finishes before user presses on stop
//                 * then automatically release resources
//                 */
//            }
//        });


    }

    private void displayProfileDetails() {
        nameDisplayField = (TextView) findViewById(R.id.nameDisplay);
        dobDisplayField = (TextView) findViewById(R.id.dobDisplay);
        genderDisplayField = (TextView) findViewById(R.id.genderDisplay);

        SharedPreferences profile = getSharedPreferences("Profile",0);
        nameDisplayField.setText(profile.getString("Name","Profile not found"));
        dobDisplayField.setText(profile.getString("DOB","Profile not found"));
        genderDisplayField.setText(profile.getString("Gender", "Profile not found"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check for existing profile
        SharedPreferences profile = getSharedPreferences("Profile",0);
        repeat = (profile.getString("Name","")).isEmpty();
        if(repeat){
            setContentView(R.layout.profile_main_repeat);
        } else{
            setContentView(R.layout.profile_main_initial);
        }
//        setContentView(R.layout.activity_main);
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

    public  void onSaveChangesClick(View view){
        //used for checking all fields are filled
        boolean error = false;
        String name = nameTextField.getText().toString();
        String dob = dobTextField.getText().toString();

        String gender = "NULL";
        int radioButtonId = genderButtons.getCheckedRadioButtonId();

        if(name.isEmpty()){
            if (nameTextField.requestFocus()) {
                nameTextField.setError("Name field is compulsory");
            } else{
                makeToast("Name field is compulsory");
            }
            error = true;
        }else if(dob.isEmpty()){
            if(dobTextField.requestFocus()){
                dobTextField.setError("Date of Birth field is compulsory");
            } else{
                makeToast("Date of Birth field is compulsory");
            }
            error = true;
        }else if(radioButtonId ==-1){
            makeToast("Gender field is compulsory");
            error =  true;
        } else {
            gender = (radioButtonId==maleButton.getId()) ? "M":"F";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            try {
                df.parse(dob);
                error = false;
            }catch (ParseException e) {
                if(dobTextField.requestFocus()) {
                    dobTextField.setError("Please enter a valid date of birth of form dd-MM-YYYY");
                    Log.d("DEBUG","error:" + e.toString());
                } else {
                    makeToast("Please enter a valid date of birth of form dd-MM-YYYY");
                }
                error = true;
            };
        }
        if(error){
            //wait for user to fill all fields
            return;
        }
        //create a profile for user
        SharedPreferences profile = getSharedPreferences("Profile",0);
        SharedPreferences.Editor editor = profile.edit();
        editor.putString("Name", name);
        editor.putString("DOB", dob);
        editor.putString("Gender", gender);
        if (editor.commit()){
            makeToast("Profile saved successfully");
            setContentView(R.layout.profile_main_repeat);
            displayProfileDetails();
        }


    }

    private void makeToast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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