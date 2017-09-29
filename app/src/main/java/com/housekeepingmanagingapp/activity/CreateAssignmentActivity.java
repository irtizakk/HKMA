package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DatabaseReference;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.dialogs.CustomAlertDialog;
import com.housekeepingmanagingapp.fragments.ViewHouseKeepersFragment;
import com.housekeepingmanagingapp.fragments.ViewRoomsFragment;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.models.ManagerModel;
import com.housekeepingmanagingapp.models.RoomModel;
import com.housekeepingmanagingapp.models.TaskModel;
import com.housekeepingmanagingapp.models.TaskStatusModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAssignmentActivity extends AppCompatActivity {


    Activity activity;
    @BindView(R.id.tvTask)
    TextView tvTask;
    @BindView(R.id.lvCreateTask)
    LinearLayout lvCreateTask;
    @BindView(R.id.btnAddMore)
    Button btnAddMore;
    @BindView(R.id.btnDone)
    Button btnDone;
    @BindView(R.id.tvRoomNo)
    TextView tvRoomNo;
    @BindView(R.id.tvHouseKeeperName)
    TextView tvHouseKeeperName;

    HouseKeeperModel houseKeeperModel;
    RoomModel roomModel;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    private int taskCounter = 0;
    private  String currentDate;

    ArrayList<TaskStatusModel> taskStatusModelArrayList = new ArrayList<>();
    private ArrayList<String> lostAndFoundItemsImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        ButterKnife.bind(this);

        activity = this;

        changeActionBarText("Create Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppController.getInstance().getPrefManger().setRoomModel(null);
        AppController.getInstance().getPrefManger().setHousekeeperProfile(null);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date date = new Date();

        currentDate = simpleDateFormat.format(date);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent, menu);
        return true;
    }

    public void changeActionBarText(String text)
    {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    @OnClick({R.id.tvRoomNo, R.id.tvHouseKeeperName, R.id.btnAddMore, R.id.btnDone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRoomNo:
                ViewRoomsFragment viewRoomsFragment = ViewRoomsFragment.newInstance("managerSelectRoomForTask");
                FragmentOpenerActivity.newInstance(viewRoomsFragment, "Select Room");
                startActivity(new Intent(activity, FragmentOpenerActivity.class));
                break;
            case R.id.tvHouseKeeperName:
                ViewHouseKeepersFragment viewHouseKeepersFragment = ViewHouseKeepersFragment.newInstance("managerSelectHouseKeeperForTask");
                FragmentOpenerActivity.newInstance(viewHouseKeepersFragment, "Select HouseKeeper");
                startActivity(new Intent(activity, FragmentOpenerActivity.class));
                break;
            case R.id.btnAddMore:

                addTaskLayout();
                break;
            case R.id.btnDone:

                if (checkValidation())
                {
                    spinKit.setVisibility(View.VISIBLE);
                    btnAddMore.setClickable(false);
                    btnDone.setText("Please Wait...");

                    for (int i = 0; i < lvCreateTask.getChildCount(); i++) 
                    {
                        View childView = lvCreateTask.getChildAt(i);
                        String taskName = ((EditText) childView.findViewById(R.id.txtTask)).getText().toString();

                        if(taskName.trim().length() > 0)
                        {
                            TaskStatusModel taskStatusModel = new TaskStatusModel();

                            taskStatusModel.setTaskName(taskName);
                            taskStatusModel.setTaskStatus("pending");
                            taskStatusModelArrayList.add(taskStatusModel);
                        }

                    }
                    FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

                    String taskId = firebaseFactory.getDatabaseRef().child("tasks").push().getKey();

                    TaskModel taskModel = new TaskModel();
                    ManagerModel managerModel = AppController.getInstance().getPrefManger().getManagerProfile();
                    taskModel.setTaskId(taskId);
                    taskModel.setCreatedByAdmin(managerModel.getCreatedByAdmin());
                    taskModel.setHousekeeperId(houseKeeperModel.getUserId());
                    taskModel.setHousekeeperName(houseKeeperModel.getFullName());
                    taskModel.setLocationId(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, "").toString());
                    taskModel.setLocationName(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_NAME, "").toString());
                    taskModel.setManagerId(managerModel.getUserId());
                    taskModel.setManagerName(managerModel.getFullName());
                    taskModel.setRoomNo(roomModel.getRoomNumber());
                    taskModel.setRoomType(roomModel.getRoomType());
                    taskModel.setRoomId(roomModel.getRoomId());
                    taskModel.setRoomFloor(roomModel.getRoomFloorNumber());
                    taskModel.setTaskMainStatus("pending");
                    taskModel.setTaskArray(taskStatusModelArrayList);
                    taskModel.setTaskAssignDate(currentDate);
                    taskModel.setTaskViewByHouseKeeper("0");
                    taskModel.setDescriptionComment("");
                    taskModel.setInspectionComment("");
                    taskModel.setHousekeeperTaskRating("0.0");
                    taskModel.setLostAndFoundItemsDescription("");
                    taskModel.setLostAndFoundItemsImages(lostAndFoundItemsImages);



                    DatabaseReference database = firebaseFactory.getDatabaseRef().child("tasks").child(taskId);
                    database.setValue(taskModel);
                    btnDone.setText("Done");
                    spinKit.setVisibility(View.GONE);
                    activity.finish();
                    Toast.makeText(activity, "Task Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private boolean checkValidation()
    {
        if(!AppConstants.isOnline(activity))
        {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(tvRoomNo.getText().toString().equals("Click To Select RoomNo"))
        {
            new CustomAlertDialog(activity,"Alert","Please Select Room Number");
            return  false;
        }
        else if(tvHouseKeeperName.getText().toString().equals("Click To Select Housekeeper"))
        {
            new CustomAlertDialog(activity,"Alert","Please Select Housekeeper");
            return  false;
        }
        else if(lvCreateTask.getChildCount() == 0)
        {
            new CustomAlertDialog(activity,"Alert","Please Write Minimum 1 Task");
            return  false;
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        houseKeeperModel = AppController.getInstance().getPrefManger().getHouseKeeperProfile();
        roomModel = AppController.getInstance().getPrefManger().getRoomModel();
        if (houseKeeperModel != null) {
            tvHouseKeeperName.setText(houseKeeperModel.getFullName());
        }
        if (roomModel != null) {
            tvRoomNo.setText(roomModel.getRoomNumber());
        }
    }

    private void addTaskLayout()
    {
        taskCounter++;
        View v = getLayoutInflater().inflate(R.layout.layout_create_task_item, null);
        v.setTag(taskCounter);

        TextView tvTask = v.findViewById(R.id.tvTask);
        tvTask.setText("Task" + taskCounter);
        ImageView ivCancel = v.findViewById(R.id.ivCancel);
        ivCancel.setTag(taskCounter);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                View v = lvCreateTask.findViewWithTag(view.getTag());
                lvCreateTask.removeView(v);

                if(lvCreateTask.getChildCount()  == 0)
                {
                    btnAddMore.setText("Add Task");
                }

                for (int i = 0; i < lvCreateTask.getChildCount(); i++)
                {
                    View childView = lvCreateTask.getChildAt(i);
                    ((TextView) childView.findViewById(R.id.tvTask)).setText("Task" + (i + 1));

                }
                taskCounter = lvCreateTask.getChildCount();

            }
        });
        lvCreateTask.addView(v);

        if(lvCreateTask.getChildCount() > 0)
        {
            btnAddMore.setText("Add More Task");
        }
    }
}
