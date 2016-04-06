package com.aubg.aubg_emergency;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    SharedPreferences myPref;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myPref = getSharedPreferences("emergency_app", MODE_PRIVATE);
        if(myPref.contains("NAME")){
            EditText txt1 = (EditText) findViewById(R.id.editText);
            txt1.setText(myPref.getString("NAME", ""));
        }
        if(myPref.contains("PHONE")){
            EditText txt2 = (EditText) findViewById(R.id.editText2);
            txt2.setText(myPref.getString("PHONE", ""));
        }
        if(myPref.contains("MAP")){
            CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox);
            checkBox1.setChecked(myPref.getBoolean("MAP", false));
        }
        if(myPref.contains("BEACON")){
            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
            checkBox2.setChecked(myPref.getBoolean("BEACON", false));
        }
    }
    public void Save(View view){
        SharedPreferences.Editor edit;
        edit = myPref.edit();
        EditText name = (EditText)findViewById(R.id.editText);
        EditText phone = (EditText)findViewById(R.id.editText2);
        CheckBox map = (CheckBox) findViewById(R.id.checkBox);
        CheckBox beacon = (CheckBox) findViewById(R.id.checkBox2);
        boolean mapMove = false;
        boolean beaconEnabled = false;
        if(name.getText().toString().trim().length()>0 && phone.getText().toString().trim().length()>0){
            edit.putString("NAME", name.getText().toString());
            edit.putString("PHONE", phone.getText().toString());
            if (map.isChecked()) {
                mapMove = true;
                edit.putBoolean("MAP", mapMove);
            } else {
                mapMove = false;
                edit.putBoolean("MAP", mapMove);
            }
            if (beacon.isChecked()) {
                beaconEnabled = true;
                edit.putBoolean("BEACON", beaconEnabled);
            } else {
                beaconEnabled = false;
                edit.putBoolean("BEACON", beaconEnabled);
            }
            edit.commit();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Can't have an empty fields", Toast.LENGTH_SHORT).show();
        }
    }
}
