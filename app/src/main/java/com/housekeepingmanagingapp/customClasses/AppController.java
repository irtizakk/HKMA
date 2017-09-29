package com.housekeepingmanagingapp.customClasses;


import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class AppController extends Application
{
    private PrefManager pref;

    public  static  int count = 0;

    private static AppController appController;
    private float scale;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseApp.initializeApp(getApplicationContext());
        appController = this;

        Picasso.Builder builder = new Picasso.Builder(this);



    }

    public static synchronized AppController getInstance()
    {
        return appController;
    }


    public PrefManager getPrefManger()
    {
        if (pref == null)
        {
            pref = new PrefManager(this);
        }

        return pref;
    }



}