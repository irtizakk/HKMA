package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.fragments.ViewAssignmentsFragment;
import com.housekeepingmanagingapp.fragments.ViewHouseKeepersFragment;
import com.housekeepingmanagingapp.fragments.ViewManagersFragment;
import com.housekeepingmanagingapp.fragments.ViewRoomsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AfterLoginAdminActivity extends AppCompatActivity
        {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.tvManagers)
    TextView tvManagers;
    @BindView(R.id.liManagers)
    LinearLayout liManagers;
    @BindView(R.id.tvHouseKeepers)
    TextView tvHouseKeepers;
    @BindView(R.id.liHouseKeepers)
    LinearLayout liHouseKeepers;
    @BindView(R.id.tvRooms)
    TextView tvRooms;
    @BindView(R.id.liRooms)
    LinearLayout liRooms;
    @BindView(R.id.tvAssignTask)
    TextView tvAssignTask;
    @BindView(R.id.liAssignTasks)
    LinearLayout liAssignTasks;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.liLogout)
    LinearLayout liLogout;
    @BindView(R.id.tvToolbarText)
    TextView tvToolbarText;


    @BindView(R.id.tvChangeLocation)
    TextView tvChangeLocation;
    @BindView(R.id.liChangeLocation)
    LinearLayout liChangeLocation;

    Activity activity;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_admin);
        ButterKnife.bind(this);

        activity = this;
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        switchFragmentWithBackStack(new ViewManagersFragment());
        tvToolbarText.setText("Managers");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @OnClick({R.id.liManagers, R.id.liHouseKeepers,R.id.liChangeLocation, R.id.liRooms, R.id.liAssignTasks, R.id.liLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liManagers:
                switchFragmentWithBackStack(new ViewManagersFragment());
                tvToolbarText.setText("Managers");
                break;
            case R.id.liHouseKeepers:
                 ViewHouseKeepersFragment viewHouseKeepersFragment = ViewHouseKeepersFragment.newInstance("adminView");
                switchFragmentWithBackStack(viewHouseKeepersFragment);
                tvToolbarText.setText("House Keepers");
                break;

            case R.id.liChangeLocation:
                activity.finish();
                startActivity(new Intent(activity,AdminViewLocationsActivity.class));

                break;
            case R.id.liRooms:
                ViewRoomsFragment viewRoomsFragment = ViewRoomsFragment.newInstance("adminView");
                switchFragmentWithBackStack(viewRoomsFragment);
                tvToolbarText.setText("Rooms");
                break;
            case R.id.liAssignTasks:
                switchFragmentWithBackStack(new ViewAssignmentsFragment());
                tvToolbarText.setText("Assignments");
                break;
            case R.id.liLogout:
                AppController.getInstance().getPrefManger().clear();
                Intent intent = new Intent(activity, SplashActivity.class);
                intent.putExtra("showOnlySplash","showOnlySplash");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                activity.finish();
                //Toast.makeText(activity, "Logout", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void switchFragmentWithBackStack(Fragment fragment)
    {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.liMain, fragment);
        fragmentTransaction.commit();
    }
}
