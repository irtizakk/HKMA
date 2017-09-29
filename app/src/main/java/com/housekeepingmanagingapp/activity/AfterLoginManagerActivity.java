package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.fragments.UserProfileFragment;
import com.housekeepingmanagingapp.fragments.ViewAssignmentsFragment;
import com.housekeepingmanagingapp.fragments.ViewHouseKeepersFragment;
import com.housekeepingmanagingapp.fragments.ViewRoomsFragment;
import com.housekeepingmanagingapp.models.ManagerModel;
import com.housekeepingmanagingapp.models.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AfterLoginManagerActivity extends AppCompatActivity {

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
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tvMessages)
    TextView tvMessages;
    @BindView(R.id.liMessages)
    LinearLayout liMessages;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.liLogout)
    LinearLayout liLogout;

    @BindView(R.id.tvToolbarText)
    TextView tvToolbarText;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    Activity activity;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.liProfile)
    LinearLayout liProfile;
    @BindView(R.id.tvLostAndFoundItems)
    TextView tvLostAndFoundItems;
    @BindView(R.id.liLostAndFoundItems)
    LinearLayout liLostAndFoundItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_manager);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        activity = this;

        fragmentTransaction = getSupportFragmentManager().beginTransaction();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
        Query query = FirebaseDatabase.getInstance().getReference("manager")
                .orderByChild("userId").equalTo(userModel.getUserId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //DataSnapshot dataSnapshot1 = dataSnapshot.getChildren();


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ManagerModel managerModel = dataSnapshot1.getValue(ManagerModel.class);

                    Log.e("test", "managerModel.getEmail() " + managerModel.getEmail());
                    System.out.println(dataSnapshot.getValue(ManagerModel.class));
                    AppController.getInstance().getPrefManger().setManagerProfile(managerModel);
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_ID, managerModel.getLocationId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_NAME, managerModel.getLocationName());

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_ID, managerModel.getUserId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_NAME, managerModel.getFullName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_IMAGE, managerModel.getProfilePic());

                    ViewHouseKeepersFragment viewHouseKeepersFragment = ViewHouseKeepersFragment.newInstance("managerDetailView");
                    switchFragmentWithBackStack(viewHouseKeepersFragment);
                    tvToolbarText.setText("HouseKeepers");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


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


    @OnClick({R.id.liHouseKeepers,R.id.tvLostAndFoundItems, R.id.liLostAndFoundItems,R.id.liProfile, R.id.liRooms, R.id.liAssignTasks, R.id.liMessages, R.id.liLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liHouseKeepers:
                tvToolbarText.setText("HouseKeepers");
                ViewHouseKeepersFragment viewHouseKeepersFragment = ViewHouseKeepersFragment.newInstance("managerDetailView");
                switchFragmentWithBackStack(viewHouseKeepersFragment);

                break;

            case R.id.liLostAndFoundItems:
                startActivity(new Intent(activity, LostAndFountItemViewActivity.class));

                break;
            case R.id.tvLostAndFoundItems:
                startActivity(new Intent(activity, LostAndFountItemViewActivity.class));

                break;
            case R.id.liRooms:
                tvToolbarText.setText("Rooms");
                ViewRoomsFragment viewRoomsFragment = ViewRoomsFragment.newInstance("managerDetailView");
                switchFragmentWithBackStack(viewRoomsFragment);
                break;
            case R.id.liProfile:
                tvToolbarText.setText("User Profile");
                switchFragmentWithBackStack(new UserProfileFragment());
                break;
            case R.id.liAssignTasks:
                tvToolbarText.setText("Tasks");
                switchFragmentWithBackStack(new ViewAssignmentsFragment());
                break;
            case R.id.liMessages:
                startActivity(new Intent(activity, ViewMessagesActivity.class));
                break;
            case R.id.liLogout:
                AppController.getInstance().getPrefManger().clear();
                Intent intent = new Intent(activity, SplashActivity.class);
                intent.putExtra("showOnlySplash", "showOnlySplash");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                activity.finish();
                // Toast.makeText(activity, "Logout", Toast.LENGTH_SHORT).show();

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void switchFragmentWithBackStack(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.liMain, fragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.tvLostAndFoundItems)
    public void onClick() {
    }
}
