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
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.models.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AfterLoginHouseKeeperActivity extends AppCompatActivity {

    @BindView(R.id.tvAssignTask)
    TextView tvAssignTask;
    @BindView(R.id.liAssignTasks)
    LinearLayout liAssignTasks;
    @BindView(R.id.tvMessages)
    TextView tvMessages;
    @BindView(R.id.liMessages)
    LinearLayout liMessages;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.liProfile)
    LinearLayout liProfile;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.liLogout)
    LinearLayout liLogout;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tvToolbarText)
    TextView tvToolbarText;

    Activity activity;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_house_keeper);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        activity = this;

        UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
        Query query = FirebaseDatabase.getInstance().getReference("housekeeper")
                .orderByChild("userId").equalTo(userModel.getUserId());

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //DataSnapshot dataSnapshot1 = dataSnapshot.getChildren();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    HouseKeeperModel houseKeeperModel = dataSnapshot1.getValue(HouseKeeperModel.class);

                    Log.e("test ","houseKeeperModel "+houseKeeperModel.getContactNumber());

                    AppController.getInstance().getPrefManger().setHousekeeperProfile(houseKeeperModel);
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_ID, houseKeeperModel.getLocationId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_NAME, houseKeeperModel.getLocationName());

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_ID,houseKeeperModel.getUserId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_NAME,houseKeeperModel.getFullName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SENDER_IMAGE, houseKeeperModel.getProfilePic());

                    tvToolbarText.setText("Tasks");
                    switchFragmentWithBackStack(new ViewAssignmentsFragment());
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




    @OnClick({R.id.liAssignTasks, R.id.liMessages, R.id.liProfile, R.id.liLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liAssignTasks:
                tvToolbarText.setText("Tasks");
                switchFragmentWithBackStack(new ViewAssignmentsFragment());
                break;
            case R.id.liMessages:
                startActivity(new Intent(activity, ViewMessagesActivity.class));
                break;
            case R.id.liProfile:
                tvToolbarText.setText("User Profile");
                switchFragmentWithBackStack(new UserProfileFragment());
                break;
            case R.id.liLogout:
                AppController.getInstance().getPrefManger().clear();
                Intent intent = new Intent(activity, SplashActivity.class);
                intent.putExtra("showOnlySplash", "showOnlySplash");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                activity.finish();
                //Toast.makeText(activity, "Logout", Toast.LENGTH_SHORT).show();

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void switchFragmentWithBackStack(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.liMain, fragment);
        fragmentTransaction.commit();
    }
}
