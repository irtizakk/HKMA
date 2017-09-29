package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.housekeepingmanagingapp.activity.interfaces.UpdateTaskInterface;
import com.housekeepingmanagingapp.adapters.ViewOnlyTaskManagerAdapter;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.models.TaskStatusModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.housekeepingmanagingapp.customClasses.AppConstants.taskModel;

public class ViewTaskDetailManagerActivity extends AppCompatActivity implements UpdateTaskInterface {
    @BindView(R.id.tvStartTaskMessage)
    TextView tvStartTaskMessage;
    @BindView(R.id.tvRoomNumber)
    TextView tvRoomNumber;
    @BindView(R.id.tvFloorNumber)
    TextView tvFloorNumber;
    @BindView(R.id.tvTask)
    TextView tvTask;
    @BindView(R.id.rvAllTask)
    RecyclerView rvAllTask;
    @BindView(R.id.txtDescription)
    EditText txtDescription;
    @BindView(R.id.tvInspectionText)
    TextView tvInspectionText;
    @BindView(R.id.txtInspection)
    EditText txtInspection;
    @BindView(R.id.tvRatingText)
    TextView tvRatingText;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    DatabaseReference database;
    Activity activity;
    ArrayList<TaskStatusModel> taskStatusModelArrayList = null;
    HouseKeeperModel houseKeeperModel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivHousekeeperImage)
    CircleImageView ivHousekeeperImage;
    @BindView(R.id.tvHouseKeeperName)
    TextView tvHouseKeeperName;
    @BindView(R.id.tvStatusAndTime)
    TextView tvStatusAndTime;
    @BindView(R.id.tvAverageRating)
    TextView tvAverageRating;
    @BindView(R.id.btnCloseTask)
    Button btnCloseTask;

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_detail_manager);
        ButterKnife.bind(this);

        activity = this;

        changeActionBarText("Room : " + AppConstants.taskModel.getRoomNo());
        setTypeFace();
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllTask.setLayoutManager(layoutManager);
        rvAllTask.setFocusable(false);

        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        database = firebaseFactory.getDatabaseRef().child("tasks").child(taskModel.getTaskId());


        getUpdateOfTask();

        ratingBar.setRating(Float.parseFloat(AppConstants.taskModel.getHousekeeperTaskRating()));

    }


    public void changeActionBarText(String text)
    {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    // --> Start setTypeFace() method.
    private void setTypeFace() {
        txtDescription.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvRoomNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvFloorNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvAverageRating.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnSave.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvTask.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

    }// <--End setTypeFace() method.


    private void getUpdateOfTask() {

        if (listState != null) {
            rvAllTask.getLayoutManager().onRestoreInstanceState(listState);
        }

        Query query = FirebaseDatabase.getInstance().getReference("housekeeper")
                .orderByChild("userId").equalTo(taskModel.getHousekeeperId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //DataSnapshot dataSnapshot1 = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    houseKeeperModel = dataSnapshot1.getValue(HouseKeeperModel.class);

                    tvHouseKeeperName.setText(houseKeeperModel.getFullName());
                    tvStatusAndTime.setText(AppConstants.taskModel.getTaskAssignDate());

                    if (houseKeeperModel.getProfilePic().equals("") || houseKeeperModel.getProfilePic() == null) {
                        ivHousekeeperImage.setImageResource(R.drawable.icon_user);
                    } else {
                        Picasso.with(activity)
                                .setIndicatorsEnabled(true);
                        Picasso.with(activity)
                                .load(houseKeeperModel.getProfilePic())
                                .placeholder(R.drawable.loader)

                                .into(ivHousekeeperImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_ID, houseKeeperModel.getUserId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_NAME, houseKeeperModel.getFullName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_IMAGE, houseKeeperModel.getProfilePic());
                    // ratingBar.setRating(Float.parseFloat(houseKeeperModel.getRating().toString()));


                    if (houseKeeperModel.getRating().toString().equals("0.0")) {
                        ratingBar.setRating(Float.parseFloat(houseKeeperModel.getRating().toString()));
                        tvAverageRating.setVisibility(View.GONE);
                    } else if (houseKeeperModel.getRating().toString().contains(",")) {
                        String[] values = houseKeeperModel.getRating().toString().split(",");
                        float totalRatings = 0.0f;
                        float ratingAverage = 0.0f;
                        for (int i = 0; i < values.length; i++) {
                            totalRatings += Float.parseFloat(values[i]);
                        }

                        ratingAverage = totalRatings / (values.length - 1);

                        tvAverageRating.setText("Currently Average Rating is " + String.format("%.2f", ratingAverage));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ratingBar.setRating(Float.parseFloat(AppConstants.taskModel.getHousekeeperTaskRating()));

        tvRoomNumber.setText("Room Number : " + AppConstants.taskModel.getRoomNo());
        tvFloorNumber.setText("Floor Number : " + AppConstants.taskModel.getRoomFloor());

        tvTask.setText("Task List : " + AppConstants.taskModel.getTaskArray().size() + " Tasks");

        if (AppConstants.taskModel.getTaskMainStatus().equals("pending"))
        {
            tvStartTaskMessage.setText("Task is in pending");
            ratingBar.setEnabled(false);
            txtInspection.setEnabled(false);
            btnCloseTask.setVisibility(View.GONE);
            tvRatingText.setText("Give Rating To Housekeeper When Tasks Closed");
            tvInspectionText.setText("Write Your Inspection When All Tasks Closed");
        } else if (AppConstants.taskModel.getTaskMainStatus().equals("inprogress"))
        {

            tvStartTaskMessage.setText("Task is in progress");
            ratingBar.setEnabled(false);
            txtInspection.setEnabled(false);
            btnCloseTask.setVisibility(View.GONE);
            tvRatingText.setText("Give Rating To Housekeeper When Tasks Closed");
            tvInspectionText.setText("Write Your Inspection When All Tasks Closed");
        } else if (AppConstants.taskModel.getTaskMainStatus().equals("completed"))
        {

            tvStartTaskMessage.setText("Task Completed");
            ratingBar.setEnabled(false);
            txtInspection.setEnabled(false);
            btnCloseTask.setVisibility(View.VISIBLE);

            tvRatingText.setText("Give Rating To Housekeeper When Tasks Closed");
            tvInspectionText.setText("Write Your Inspection When All Tasks Closed");
        }
        else if (AppConstants.taskModel.getTaskMainStatus().equals("closed"))
        {

            tvStartTaskMessage.setText("Task Closed");
            txtInspection.setText(AppConstants.taskModel.getInspectionComment());
            btnCloseTask.setVisibility(View.GONE);
            ratingBar.setEnabled(true);
            txtInspection.setEnabled(true);

            tvRatingText.setText("Give Rating To Housekeeper Now");
            tvInspectionText.setText("Write Your Inspection Comments Now");
        }
        if (AppConstants.taskModel.getDescriptionComment().trim().equals(""))
        {
            txtDescription.setHint("No Description Available");
        }
        else
        {
            txtDescription.setText(AppConstants.taskModel.getDescriptionComment());
        }
        taskStatusModelArrayList = AppConstants.taskModel.getTaskArray();

        ViewOnlyTaskManagerAdapter adapter = new ViewOnlyTaskManagerAdapter(activity, taskStatusModelArrayList);
        rvAllTask.setAdapter(adapter);
    }

    private BroadcastReceiver onUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("action").equals(AppConstants.ON_TASK_UPDATE)) {
                getUpdateOfTask();
            }

        }
    };


    @Override
    public void onResume() {
        super.onResume();

        // Broadcast receiver
        LocalBroadcastManager.getInstance(activity).registerReceiver(onUpdateReceiver, new IntentFilter("onUpdateManager"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Broadcast receiver
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(onUpdateReceiver);
    }

    private void saveUpdate() {
        spinKit.setVisibility(View.VISIBLE);
        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("housekeeper").child(houseKeeperModel.getUserId());

        database2.child("rating").setValue(houseKeeperModel.getRating() + "," + ratingBar.getRating() + "");

        AppConstants.taskModel.setInspectionComment(txtInspection.getText().toString());
        AppConstants.taskModel.setDescriptionComment(txtDescription.getText().toString());
        AppConstants.taskModel.setHousekeeperTaskRating(ratingBar.getRating() + "");

        database.setValue(AppConstants.taskModel);

        spinKit.setVisibility(View.GONE);
        Toast.makeText(activity, "Updated/Save", Toast.LENGTH_SHORT).show();


    }

    @OnClick({R.id.btnSave, R.id.ivMessage,R.id.btnCloseTask})
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnCloseTask:
                showDialog();
                break;
            case R.id.ivMessage:
                startActivity(new Intent(activity, ActivityChat.class));
                break;
            case R.id.btnSave:
                if (AppConstants.isOnline(activity)) {
                    saveUpdate();

                } else {
                    Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


   private void showDialog()
    {
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity);

        builder.setTitle("Alert");
        builder.setMessage("Are You Sure You Want To Close This Task?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                AppConstants.taskModel.setTaskMainStatus("closed");
                database.setValue(AppConstants.taskModel);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void update(TaskStatusModel taskStatusModel, int position)
    {
            listState = rvAllTask.getLayoutManager().onSaveInstanceState();

            spinKit.setVisibility(View.VISIBLE);

            ArrayList<TaskStatusModel> taskStatusModelArrayList = AppConstants.taskModel.getTaskArray();

            taskStatusModelArrayList.set(position, taskStatusModel);

            AppConstants.taskModel.setTaskArray(taskStatusModelArrayList);
            AppConstants.taskModel.setTaskMainStatus("inprogress");

            database.setValue(AppConstants.taskModel);

            spinKit.setVisibility(View.GONE);

    }
}
