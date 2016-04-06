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

public class howDoIKnowThreat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_do_i_know_threat);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment fr;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0:
                        fr = new Followed();
                        fragmentTransaction.replace(R.id.fragment, fr);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fr = new Conflict();
                        fragmentTransaction.replace(R.id.fragment, fr);
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fr = new Bullied();
                        fragmentTransaction.replace(R.id.fragment, fr);
                        fragmentTransaction.commit();
                        break;
                    default:
                        fr = new Followed();
                        fragmentTransaction.replace(R.id.fragment, fr);
                        fragmentTransaction.commit();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        List<String> categories = new ArrayList<>();
        categories.add("Someone is being followed");
        categories.add("A conflict between two people");
        categories.add("Someone is getting bullied");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner, categories);

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
