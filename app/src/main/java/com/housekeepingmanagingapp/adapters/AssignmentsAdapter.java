package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.ViewTaskDetailHousekeeperActivity;
import com.housekeepingmanagingapp.activity.ViewTaskDetailManagerActivity;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.models.TaskModel;
import com.housekeepingmanagingapp.models.UserModel;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.MyViewHolder> {

    Activity activity;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvHouseKeeper)
    TextView tvHouseKeeper;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvTime)
    TextView tvTime;
    private String key;

    private int newTaskCounter = 0;
    UserModel userModel;


    private LinkedHashMap<String, TaskModel> taskModelLArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);

        }
    }

    public AssignmentsAdapter(Activity activity, LinkedHashMap<String, TaskModel> taskModelLArrayList) {

        this.activity = activity;
        this.taskModelLArrayList = taskModelLArrayList;
        userModel = AppController.getInstance().getPrefManger().getUserProfile();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_assignment_item, parent, false);

        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        tvRoom.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvHouseKeeper.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvTime.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

        key = taskModelLArrayList.keySet().toArray()[position].toString();

        TaskModel taskModel = taskModelLArrayList.get(key);

        tvRoom.setText(taskModel.getRoomNo());
        String date = taskModel.getTaskAssignDate().substring(0, taskModel.getTaskAssignDate().indexOf(" "));
        String time = taskModel.getTaskAssignDate().substring(taskModel.getTaskAssignDate().indexOf(" ") + 1);
        tvStatus.setText(taskModel.getTaskMainStatus());
        tvTime.setText(date + "\n" + time);

        if (userModel.getRole().equals("housekeeper"))
        {
            tvHouseKeeper.setText(taskModel.getManagerName());
        } else if (userModel.getRole().equals("manager"))
        {
            tvHouseKeeper.setText(taskModel.getHousekeeperName());
        }
//        if(userModel.getRole().equals("housekeeper"))
//        {
//            if(taskModel.getTaskViewByHouseKeeper().equals("0"))
//            {
//                newTaskCounter++;
//                FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();
//                DatabaseReference database = firebaseFactory.getDatabaseRef().child("tasks").child(taskModel.getTaskId());
//                database.child("taskViewByHouseKeeper").setValue("1");
//
//
//            }
//
//
//        }

//        if(position == taskModelLArrayList.size()-1)
//        {
//            new CustomAlertDialog(activity,"New Task","You Have "+newTaskCounter+" New Task");
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                key = taskModelLArrayList.keySet().toArray()[position].toString();

                AppConstants.taskModel = taskModelLArrayList.get(key);

                if (userModel.getRole().equals("housekeeper")) {
                    activity.startActivity(new Intent(activity, ViewTaskDetailHousekeeperActivity.class));
                } else {
                    activity.startActivity(new Intent(activity, ViewTaskDetailManagerActivity.class));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return taskModelLArrayList.size();
    }

}