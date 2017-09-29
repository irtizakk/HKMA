package com.housekeepingmanagingapp.customClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.housekeepingmanagingapp.models.TaskModel;

/**
 * Created by Irtiza on 7/29/2017.
 */
public class AppConstants
{

    public static TaskModel taskModel;
    public static final String ON_TASK_UPDATE = "onTaskUpdate";

    // This methos is used to check user is connected to internet or not
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;

        return false;
    }
}
