package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DatabaseReference;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.RoomModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddandEditRoomActivity extends AppCompatActivity {

    @BindView(R.id.txtRoomNo)
    EditText txtRoomNo;
    @BindView(R.id.txtFloorNo)
    EditText txtFloorNo;
    @BindView(R.id.txtRoomType)
    EditText txtRoomType;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.rlEditAndDelete)
    RelativeLayout rlEditAndDelete;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.activity_addand_edit_house_keeper)
    RelativeLayout activityAddandEditHouseKeeper;
    private String action = "", choice = "";

    Activity activity;

    RoomModel roomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addand_edit_room);
        ButterKnife.bind(this);

        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTypeFace();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

            choice = bundle.getString("choice");
            if (choice.equals("adminView"))
            {
                action = bundle.getString("action");

                if (action.equals("edit"))
                {
                    btnSave.setVisibility(View.GONE);
                    changeActionBarText("Edit Room");

                    roomModel = (RoomModel) getIntent()
                            .getParcelableExtra("roomModel");

                    txtRoomType.setText(roomModel.getRoomType());
                    txtFloorNo.setText(roomModel.getRoomFloorNumber());
                    txtRoomNo.setText(roomModel.getRoomNumber());



                } else if (action.equals("add")) {
                    rlEditAndDelete.setVisibility(View.GONE);
                    changeActionBarText("Add Room");
                }
            }
            else if (choice.equals("managerDetailView"))
            {
                btnSave.setVisibility(View.GONE);
                changeActionBarText("View Room Detail");

                roomModel = (RoomModel) getIntent()
                        .getParcelableExtra("roomModel");

                txtRoomType.setText("Room Type "+roomModel.getRoomType());
                txtFloorNo.setText("Floor No "+roomModel.getRoomFloorNumber());
                txtRoomNo.setText("Room No "+roomModel.getRoomNumber());

                txtRoomNo.setEnabled(false);
                txtFloorNo.setEnabled(false);
                txtRoomType.setEnabled(false);
                rlEditAndDelete.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent, menu);
        return true;
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


    public void changeActionBarText(String text) {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }


    @OnClick({R.id.btnSave, R.id.btnEdit, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSave:
                if(checkValidation())
                {
                    spinKit.setVisibility(View.VISIBLE);

                    FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();
                    String roomId = firebaseFactory.getDatabaseRef().child("rooms").push().getKey();
                    DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("rooms").child(roomId);
                    database2.child("roomNumber").setValue(txtRoomNo.getText().toString());
                    database2.child("roomType").setValue(txtRoomType.getText().toString());
                    database2.child("roomFloorNumber").setValue(txtFloorNo.getText().toString());
                    database2.child("locationId").setValue(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, ""));
                    database2.child("locationName").setValue(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_NAME, ""));
                    database2.child("roomId").setValue(roomId);
                    database2.child("createdByAdmin").setValue(AppController.getInstance().getPrefManger().getUserProfile().getUserId());
                    // database2.child("date").setValue(currentDate);
                    spinKit.setVisibility(View.GONE);


                    Toast.makeText(activity, "Successfully Created Room", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case R.id.btnEdit:
                if(checkValidation())
                {

                    FirebaseFactory firebaseFactory2 = FirebaseFactory.getFirebaseFactory();
                    String roomId = roomModel.getRoomId();
                    DatabaseReference database3 = firebaseFactory2.getDatabaseRef().child("rooms").child(roomId);
                    database3.child("roomNumber").setValue(txtRoomNo.getText().toString());
                    database3.child("roomType").setValue(txtRoomType.getText().toString());
                    database3.child("roomFloorNumber").setValue(txtFloorNo.getText().toString());

                    spinKit.setVisibility(View.GONE);


                    Toast.makeText(activity, "Successfully Updated Room", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case R.id.btnDelete:

                if(AppConstants.isOnline(activity))
                {
                    FirebaseFactory firebaseFactory3 = FirebaseFactory.getFirebaseFactory();
                    String roomId = roomModel.getRoomId();
                    DatabaseReference database4 = firebaseFactory3.getDatabaseRef().child("rooms").child(roomId);
                    database4.removeValue();
                    Toast.makeText(activity, "Successfully Deleted Room", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private boolean checkValidation()
    {
        if (!AppConstants.isOnline(activity))
        {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

            return false;
        } else if (txtRoomNo.getText().toString().trim().length() ==0)
        {

            Toast.makeText(activity, "Enter Correct Room Number", Toast.LENGTH_LONG).show();

            return false;
        } else if (txtFloorNo.getText().toString().trim().length() ==0)
        {

            Toast.makeText(activity, "Enter Correct Floor Number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (txtRoomType.getText().toString().trim().length() ==0)
        {

            Toast.makeText(activity, "Enter Correct Room Type", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    // --> Start setTypeFace() method.
    private void setTypeFace() {
        txtRoomType.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtRoomType.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtRoomNo.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnDelete.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnSave.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnEdit.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
    }// <--End setTypeFace() method.
}
