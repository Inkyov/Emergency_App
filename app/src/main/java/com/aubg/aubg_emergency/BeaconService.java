package com.aubg.aubg_emergency;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class BeaconService extends Service {

    protected LocationManager lm;
    protected LocationListener ll=new myLocationListener();
    Location myLocation;
    String phoneNumber;
    String Name;
    SharedPreferences myPref;
    boolean beacon;
    Handler beaconHandler = new Handler();
    Timer timer = new Timer();


    public BeaconService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, ll);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
        }
        myPref = getSharedPreferences("emergency_app", MODE_PRIVATE);

        Name = myPref.getString("NAME", "A STUDENT");
        phoneNumber = myPref.getString("PHONE", "5556");
        beacon = myPref.getBoolean("BEACON", false);

        makeUseOfNewLocation(myLocation);
        if (!beacon) {
            lm.removeUpdates(ll);
            Kill();
        } else {

                beaconHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myPref = getSharedPreferences("emergency_app", MODE_PRIVATE);
                        beacon = myPref.getBoolean("BEACON", false);
                        if (beacon) {
                            makeUseOfNewLocation(myLocation);
                            beaconHandler.postDelayed(this, 180000);
                        } else {
                            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                lm.removeUpdates(ll);
                            }
                            beaconHandler.removeCallbacksAndMessages(this);
                            Kill();
                        }

                    }
                }, 180000);

            }

        return 1;
    }
@Override
    public void onDestroy(){
        super.onDestroy();
    }
    private void Kill(){
        this.stopSelf();
    }
    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider
            //Used only to assign the location to the global variable
            myLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    public void makeUseOfNewLocation(Location location) {
        //displays the GPS updates on a toast
        Context context = getApplicationContext();
        String text;
        if (location == null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null) {
                text = Name +" needs help at: "+ location.getLatitude() + " lat\n" + location.getLongitude() + " long\n" + location.getAltitude() + " alt";
                sendSMS(text);
            }
            else
                text = "Can't get current location. ";

        }
        else {
            text = Name +" needs help at: "+ location.getLatitude() + " lat\n" + location.getLongitude() + " long\n" + location.getAltitude() + " alt";
            sendSMS(text);
        }
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
    public void sendSMS(String text){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
        }
        else {
            //send Text message with current location to a pre-defined number
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
}
