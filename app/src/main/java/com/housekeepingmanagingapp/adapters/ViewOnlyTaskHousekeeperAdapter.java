package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.interfaces.UpdateTaskInterface;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.models.TaskStatusModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class ViewOnlyTaskHousekeeperAdapter extends RecyclerView.Adapter<ViewOnlyTaskHousekeeperAdapter.MyViewHolder> {


    Activity activity;
    UpdateTaskInterface updateTaskInterface;


    private ArrayList<TaskStatusModel> taskStatusModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.chTaskDone)
        CheckBox chTaskDone;
        @BindView(R.id.tvTaskNumber)
        TextView tvTaskNumber;
        @BindView(R.id.tvTaskName)
        TextView tvTaskName;
        @BindView(R.id.tvTaskDone)
        TextView tvTaskDone;

        public MyViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
    public ViewOnlyTaskHousekeeperAdapter(Activity activity, ArrayList<TaskStatusModel> taskStatusModelArrayList) {

        this.activity = activity;
        updateTaskInterface = (UpdateTaskInterface) activity;
        this.taskStatusModelArrayList = taskStatusModelArrayList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_show_task_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        TaskStatusModel taskStatusModel = taskStatusModelArrayList.get(position);

        if (taskStatusModel.getTaskStatus().equals("done"))
        {

            //holder.itemView.setBackgroundColor(Color.parseColor("#666666"));
            holder.chTaskDone.setButtonDrawable(R.drawable.icon_chtick);
        }
        else
        {
            holder.chTaskDone.setButtonDrawable(R.drawable.icon_chuntick);
        }
        holder.tvTaskNumber.setText("Task" + (position + 1));
        holder.tvTaskName.setText(taskStatusModel.getTaskName());

        holder.chTaskDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TaskStatusModel taskStatusModel = taskStatusModelArrayList.get(position);
                if(taskStatusModel.getTaskStatus().equals("done") && !AppConstants.taskModel.getTaskMainStatus().equals("completed") && !AppConstants.taskModel.getTaskMainStatus().equals("closed"))
                {
                    taskStatusModel.setTaskStatus("pending");
                    updateTaskInterface.update(taskStatusModel, position);
                }
                else if(taskStatusModel.getTaskStatus().equals("pending") && !AppConstants.taskModel.getTaskMainStatus().equals("completed") && !AppConstants.taskModel.getTaskMainStatus().equals("closed"))
                {
                    taskStatusModel.setTaskStatus("done");
                    updateTaskInterface.update(taskStatusModel, position);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return taskStatusModelArrayList.size();
    }

}