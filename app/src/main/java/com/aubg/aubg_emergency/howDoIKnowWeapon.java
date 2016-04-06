package com.aubg.aubg_emergency;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class howDoIKnowWeapon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_do_i_know_weapon);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment fr;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0:
                        fr = new Knives();
                        fragmentTransaction.replace(R.id.fragment1, fr);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fr = new Bat();
                        fragmentTransaction.replace(R.id.fragment1, fr);
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fr = new Gun();
                        fragmentTransaction.replace(R.id.fragment1, fr);
                        fragmentTransaction.commit();
                        break;
                    default:
                        fr = new Knives();
                        fragmentTransaction.replace(R.id.fragment1, fr);
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> categories = new ArrayList<>();
        categories.add("Knife/Sword/Machete");
        categories.add("Bat/Baton/Stick");
        categories.add("Any type of gun");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
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
