package com.sinduran.heartrate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by vishaal on 11/11/15.
 */
public class ProfileActivity extends Activity {
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
        repeat = false;
        //check for existing profile
        SharedPreferences profile = getSharedPreferences("Profile",0);
        repeat = !(profile.getString("Name","NOT FOUND")).equalsIgnoreCase("NOT FOUND");
        if(repeat){
            Log.d("DEBUG", "entering repeated section, REPEAT BOOLEAN IS "  + repeat + " name is " + profile.getString("Name",""));
            setContentView(R.layout.profile_main_repeat);
        } else{
            Log.d("DEBUG", "entering initial section");
            setContentView(R.layout.profile_main_initial);
        }
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
    }

    private void displayProfileDetails() {
        nameDisplayField = (TextView) findViewById(R.id.nameDisplay);
        dobDisplayField = (TextView) findViewById(R.id.dobDisplay);
        genderDisplayField = (TextView) findViewById(R.id.genderDisplay);
        Log.d("DEBUG", "DISPLAYING PROFILE DETAILS");

        SharedPreferences profile = getSharedPreferences("Profile",0);
        nameDisplayField.setText(profile.getString("Name","Profile not found"));
        dobDisplayField.setText(profile.getString("DOB","Profile not found"));
        genderDisplayField.setText(profile.getString("Gender", "Profile not found"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public void onSaveChangesClick(View view){
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
                } else {
                    makeToast("Please enter a valid date of birth of form dd-MM-YYYY");
                }
                error = true;
            }
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

}
