package com.aubg.aubg_emergency;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class getHelp extends AppCompatActivity {
    Activity myActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //Intent calling the threatOfViolence activity
    public void threatOfViolence(View view){
        Intent intent = new Intent(this, threatOfViolence.class);
       try{
            startActivity(intent);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "There was a problem running your activity!", Toast.LENGTH_SHORT).show();
        }
    }

    //Intent calling the weaponOnCampus activity
    public void weaponOnCampus(View view){
        Intent intent = new Intent(this, weaponOnCampus.class);
        try{
            startActivity(intent);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "There was a problem running your activity!", Toast.LENGTH_SHORT).show();
        }
    }

    public void emergencyMap(View view){
        Activity myActivity = this;

        try{
            Intent intent = new Intent(this, EmergencyMap.class);
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            startActivity(intent);
        }catch(android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "There was a problem running your activity!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.d("ERROR: ", e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
