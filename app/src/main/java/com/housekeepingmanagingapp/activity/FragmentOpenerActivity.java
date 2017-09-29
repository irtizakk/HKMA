package com.housekeepingmanagingapp.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;


public class FragmentOpenerActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private static Fragment fragment;
    Activity activity;
    private static String titleOfActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_opener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;


        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        ((TextView) findViewById(R.id.tvActionBarTitle)).setText(titleOfActivity);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }

    public static void newInstance(Fragment frag,String title)
    {
       fragment = frag;
       titleOfActivity = title;
    }
    // End Of above method






}
