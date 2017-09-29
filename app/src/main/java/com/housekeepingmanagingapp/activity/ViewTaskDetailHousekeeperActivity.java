package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.interfaces.UpdateTaskInterface;
import com.housekeepingmanagingapp.adapters.ViewOnlyTaskHousekeeperAdapter;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.dialogs.CustomAlertDialog;
import com.housekeepingmanagingapp.models.ManagerModel;
import com.housekeepingmanagingapp.models.TaskStatusModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.housekeepingmanagingapp.customClasses.AppConstants.taskModel;

public class ViewTaskDetailHousekeeperActivity extends AppCompatActivity implements UpdateTaskInterface {

    @BindView(R.id.tvTask)
    TextView tvTask;
    @BindView(R.id.rvAllTask)
    RecyclerView rvAllTask;
    Activity activity;
    @BindView(R.id.tvLostAndFound)
    TextView tvLostAndFound;
    @BindView(R.id.tvUploadImage)
    TextView tvUploadImage;
    @BindView(R.id.liUploadImageOfLostOrFound)
    LinearLayout liUploadImageOfLostOrFound;
    @BindView(R.id.txtDescription)
    EditText txtDescription;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;


    ArrayList<TaskStatusModel> taskStatusModelArrayList = null;
    @BindView(R.id.tvRoomNumber)
    TextView tvRoomNumber;
    @BindView(R.id.tvFloorNumber)
    TextView tvFloorNumber;
    @BindView(R.id.tvTaskMessage)
    TextView tvTaskMessage;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.btnStartTask)
    Button btnStartTask;
    private Parcelable listState;
    DatabaseReference database;
    @BindView(R.id.tvStartTaskMessage)
    TextView tvStartTaskMessage;

    int position=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housekeeper_view_task_detail);
        ButterKnife.bind(this);


        activity = this;

        changeActionBarText("Task Details");
        setTypeFace();
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllTask.setLayoutManager(layoutManager);
        rvAllTask.setFocusable(false);


        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        database = firebaseFactory.getDatabaseRef().child("tasks").child(taskModel.getTaskId());

        Query query = FirebaseDatabase.getInstance().getReference("manager")
                .orderByChild("userId").equalTo(taskModel.getManagerId());

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //DataSnapshot dataSnapshot1 = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    ManagerModel managerModel = dataSnapshot1.getValue(ManagerModel.class);

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_ID, managerModel.getUserId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_NAME, managerModel.getFullName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_IMAGE, managerModel.getProfilePic());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }

        });
        getUpdateOfTask();
    }


    private void getUpdateOfTask()
    {
        if(listState != null)
        {
            rvAllTask.getLayoutManager().onRestoreInstanceState(listState);
        }

        taskModel = taskModel;


        tvRoomNumber.setText("Room Number : " + AppConstants.taskModel.getRoomNo());
        tvFloorNumber.setText("Floor Number : " + AppConstants.taskModel.getRoomFloor());

        tvTask.setText("Task List : "+AppConstants.taskModel.getTaskArray().size()+" Tasks");

        if (AppConstants.taskModel.getTaskMainStatus().equals("pending"))
        {
            btnStartTask.setText("START TASK");
            tvStartTaskMessage.setText("Click On Start Task Button ");
            liUploadImageOfLostOrFound.setEnabled(false);
        } else if (AppConstants.taskModel.getTaskMainStatus().equals("inprogress"))
        {
            btnStartTask.setText("COMPLETE TASK");
            tvStartTaskMessage.setText("After Completing All Tasks Click On Complete Task");
            liUploadImageOfLostOrFound.setEnabled(true);
        }
        else if (AppConstants.taskModel.getTaskMainStatus().equals("completed"))
        {
            btnStartTask.setVisibility(View.GONE);

            tvStartTaskMessage.setText("THIS TASK IS COMPLETED");

            liUploadImageOfLostOrFound.setEnabled(false);
        }

        else if (AppConstants.taskModel.getTaskMainStatus().equals("closed"))
        {
            btnStartTask.setVisibility(View.GONE);

            tvStartTaskMessage.setText("THIS TASK IS CLOSED");

            liUploadImageOfLostOrFound.setEnabled(false);
        }


        if(AppConstants.taskModel.getDescriptionComment().trim().equals(""))
        {
            txtDescription.setText("No Description Available");
        }
        else
        {
            txtDescription.setText(AppConstants.taskModel.getDescriptionComment());
        }
        taskStatusModelArrayList = AppConstants.taskModel.getTaskArray();

        ViewOnlyTaskHousekeeperAdapter adapter = new ViewOnlyTaskHousekeeperAdapter(activity, taskStatusModelArrayList);
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

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
    private void setTypeFace()
    {

        txtDescription.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvRoomNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvFloorNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvLostAndFound.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvUploadImage.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnSave.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvTask.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

    }// <--End setTypeFace() method.

    @OnClick({R.id.liUploadImageOfLostOrFound, R.id.btnStartTask, R.id.btnSave, R.id.ivMessage})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.liUploadImageOfLostOrFound:
                startActivity(new Intent(activity,LostAndFoundItemUploadActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
                break;
            case R.id.btnSave:
                break;
            case R.id.btnStartTask:

                startAndCompleteTask(btnStartTask.getText().toString());

                break;
            case R.id.ivMessage:

                startActivity(new Intent(activity, ActivityChat.class));
                break;
        }
    }



    private void startAndCompleteTask(String taskType)
    {
        if (taskType.equals("START TASK"))
        {
            AppConstants.taskModel.setTaskMainStatus("inprogress");
            database.setValue(AppConstants.taskModel);

            btnStartTask.setText("COMPLETE TASK");
        }
        else if (taskType.equals("COMPLETE TASK"))
        {
            boolean alltaskCompleted = false;

            for (TaskStatusModel taskStatusModel : AppConstants.taskModel.getTaskArray())
            {
                if (taskStatusModel.getTaskStatus().equalsIgnoreCase("pending"))
                {
                    alltaskCompleted = false;

                    break;
                }
                else
                {
                    alltaskCompleted = true;
                }
            }

            if (alltaskCompleted == false)
            {
                new CustomAlertDialog(activity, "Alert", "Please Complete All Task");
            }
            else
            {
                AppConstants.taskModel.setTaskMainStatus("completed");
                database.setValue(AppConstants.taskModel);

            }
        }


    }


    @Override
    public void update(TaskStatusModel taskStatusModel, int position)
    {

        if(AppConstants.taskModel.getTaskMainStatus().equals("pending"))
        {
            new CustomAlertDialog(activity,"Alert","Please Click On Start Task First");
        }
        else if(AppConstants.taskModel.getTaskMainStatus().equals("inprogress"))
        {
            listState = rvAllTask.getLayoutManager().onSaveInstanceState();

            spinKit.setVisibility(View.VISIBLE);

            ArrayList<TaskStatusModel> taskStatusModelArrayList = AppConstants.taskModel.getTaskArray();

            taskStatusModelArrayList.set(position, taskStatusModel);

            AppConstants.taskModel.setTaskArray(taskStatusModelArrayList);

            database.setValue(AppConstants.taskModel);

            spinKit.setVisibility(View.GONE);
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Broadcast receiver
        LocalBroadcastManager.getInstance(activity).registerReceiver(onUpdateReceiver, new IntentFilter("onUpdate"));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // Broadcast receiver
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(onUpdateReceiver);
    }
}
