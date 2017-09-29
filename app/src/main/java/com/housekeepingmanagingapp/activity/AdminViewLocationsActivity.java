package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminViewLocationsActivity extends AppCompatActivity
{


    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivAddLocation)
    ImageView ivAddLocation;

    Activity activity;
    @BindView(R.id.tvLogo)
    TextView tvLogo;
    @BindView(R.id.spinnerLocations)
    Spinner spinnerLocations;
    @BindView(R.id.btnContinue)
    Button btnContinue;

    

    //This is the location name which will be add by dialog to server
    private String addlocationName="";
    Dialog dialog;

    // Saving Location Names in this ArrayList
     ArrayList<String>  locationsNameArrayList = new ArrayList<>();
    ArrayList<String>  locationsKeyArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_locations);
        ButterKnife.bind(this);

        activity = this;

        changeActionBarText("Select Location");

        // getting all locations from server
        getAllLocations();

    }

    // --> Start  addLocationDialog() method
    private void addLocationDialog()
    {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_location, null);
        dialog = new Dialog(activity);
        dialog.setContentView(view);

        Button btnAddLocation = (Button) view.findViewById(R.id.btnAddLocation);
        final EditText txtLocationName = (EditText) view.findViewById(R.id.txtLocationName);

        btnAddLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(AppConstants.isOnline(activity))
                {
                    if(txtLocationName.getText().toString().trim().length() != 0)
                    {
                        addlocationName = txtLocationName.getText().toString();
                        addLocation();
                    }

                }
                else
                {
                    Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        //mergeViews(view);

        dialog.getWindow().getAttributes().windowAnimations = R.style.customDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }// <-- End of addLocationDialog() method

    public void changeActionBarText(String text)
    {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    @OnClick({R.id.btnContinue, R.id.ivAddLocation})
    public void onClick(View view) 
    {
        switch (view.getId()) 
        {
            case R.id.btnContinue:
                if(locationsNameArrayList.size() > 0)
                {
                    String locationName="";
                    locationName = (spinnerLocations.getSelectedItem()).toString();
                    String locationId = locationsKeyArrayList.get(spinnerLocations.getSelectedItemPosition());

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_ID,locationId);
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_SELECTED_LOCATION_NAME,locationName);

                    if(locationName.equals(""))
                    {
                        Toast.makeText(activity, "Please Select A Location To Continue", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        startActivity(new Intent(activity,AfterLoginAdminActivity.class));
                        activity.finish();
                    }
                }
                else
                {
                    Toast.makeText(activity, "Please Add A Location First", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivAddLocation:
                if (AppConstants.isOnline(activity))
                {
                    addLocationDialog();
                }
                else
                {
                    Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getAllLocations()
    {

        spinKit.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("locations")
                .orderByChild("createdByAdmin").equalTo(AppController.getInstance().getPrefManger().getUserProfile().getUserId());

        query.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                locationsNameArrayList.clear();
                locationsKeyArrayList.clear();

                if(dataSnapshot.hasChildren())
                {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren())
                    {
                        locationsNameArrayList.add(dataSnap.child("locationName").getValue().toString());
                        locationsKeyArrayList.add(dataSnap.getKey());

                    }
                }
                else
                {
                    Toast.makeText(activity, "No Locations Available Please Add a  Location To Continue", Toast.LENGTH_SHORT).show();
                }
                
                ArrayAdapter arrayAdapter = new ArrayAdapter(activity,R.layout.my_spinner_style,locationsNameArrayList);
                arrayAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_style);
                spinnerLocations.setAdapter(arrayAdapter);

                spinKit.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                spinKit.setVisibility(View.GONE);
            }
        });
    }

    public void addLocation()
    {

       try
       {
            spinKit.setVisibility(View.VISIBLE);

            FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();


            DatabaseReference database = firebaseFactory.getDatabaseRef().child("locations");


            String locationId = database.push().getKey();


            database = database.child(locationId);

            database.child("locationId").setValue(locationId);

            database.child("locationName").setValue(addlocationName);

            database.child("createdByAdmin").setValue(AppController.getInstance().getPrefManger().getUserProfile().getUserId());

            dialog.dismiss();
            spinKit.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            Log.e("test","10");
        }

    }
}
