/*
* Main activity of AUBG Emergency App
* Developed by Tomislav Marinov and
* Martin Inkyov for INF397, Mobile
* Computing at AUBG.
*
* DO NOT DISTRIBUTE
*
* If you don't want for this test
* version to send out texts like crazy
* comment out the else part of the
* function makeUseOfNewLocation().
*
* DO NOT PUT ON PHONES AS IS!
*
* Only run in emulator with second
* emulator running on port 5556 to
* catch incoming text messages.
*
*/

package com.aubg.aubg_emergency;

//TODO: Once the button is pressed for more than 5 seconds, make the phone into a beacon that sends GPS updates every 1 minute. Add that as option in settings.

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.os.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends AppCompatActivity {


    Intent intent;
    Activity myActivity = this;
    CountDownTimer myCountdown;
    String phoneNumber;
    String Name;
    boolean beaconValue;
    SharedPreferences myPref;
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //don't really need to take any action. this will request permissions until they are granted
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //don't really need to take any other action. this will request permissions until they are granted
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, Register.class);
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Your Activity is not found", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onResume() {
        super.onResume();

        //sets up variables for the Buttons so we can use them in the Java code
        final Button myHelp = (Button) findViewById(R.id.button);
        final Button myEmergency = (Button) findViewById(R.id.button2);

        myPref = getSharedPreferences("emergency_app", MODE_PRIVATE);
        if(!myPref.contains("NAME")){
            Intent intent = new Intent(this, Register.class);
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Your Activity is not found", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Name = myPref.getString("NAME", "A STUDENT");
            phoneNumber = myPref.getString("PHONE", "5556");
            beaconValue = myPref.getBoolean("BEACON", false);
        }

        intent = new Intent(getBaseContext(), BeaconService.class);
        //creates an event listener for the onTouch event, which enables us to have a delay to check if the user is
        //holding down the button and not accidentally pressing it
        myEmergency.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //sets up a timer so we can have a delay
                        myCountdown = new CountDownTimer(5000, 1000) {
                            //set up a counter to tell the user how long to hold
                            int currentCounter = 5;

                            @Override
                            //on tick change text of unused button
                            public void onTick(long millisUntilFinished) {
                                currentCounter -= 1;
                                myHelp.setText("Hold the button for " + currentCounter + " more seconds!");
                            }

                            @Override
                            public void onFinish() {
                                currentCounter = 5;
                                startService(new Intent(MainActivity.this, BeaconService.class));
                            }
                        };

                        //check if we have the needed permissions to access the location services
                        if (ActivityCompat.checkSelfPermission(myActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            return false;
                        }
                        else {
                            myCountdown.start();
                        }
                        break;
                    //If button is released, cancel the timer and just in case set the flag to false
                    case MotionEvent.ACTION_UP:
                        myCountdown.cancel();
                        myHelp.setText("GET HELP");
                        break;
                }
                return false;
            }
        });
        
    }

    @Override
    protected void onPause() {
        super.onPause();
            }

    public void getHelp(View view) {
        Intent intent = new Intent(this, getHelp.class);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Your Activity is not found", Toast.LENGTH_SHORT).show();
        }
    }

}
