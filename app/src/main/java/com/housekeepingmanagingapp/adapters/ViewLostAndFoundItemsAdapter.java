package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.LostAndFoundItemUploadActivity;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.models.TaskModel;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class ViewLostAndFoundItemsAdapter extends RecyclerView.Adapter<ViewLostAndFoundItemsAdapter.MyViewHolder> {

    Activity activity;
    private LinkedHashMap<String, TaskModel> taskModelLArrayList;
    private  String key;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHouseKeeperName)
        TextView tvHouseKeeperName;
        @BindView(R.id.tvRoomNumber)
        TextView tvRoomNumber;
        @BindView(R.id.tvRoomFloorNumber)
        TextView tvRoomFloorNumber;
        public MyViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ViewLostAndFoundItemsAdapter(Activity activity, LinkedHashMap<String, TaskModel> taskModelLArrayList)
    {

        this.activity = activity;
        this.taskModelLArrayList = taskModelLArrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_lost_and_found_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        key = taskModelLArrayList.keySet().toArray()[position].toString();
        TaskModel taskModel = taskModelLArrayList.get(key);

        holder.tvHouseKeeperName.setText("Housekeeper Name :"+taskModel.getHousekeeperName());
        holder.tvRoomNumber.setText("Room Number : "+taskModel.getRoomNo());
        holder.tvRoomFloorNumber.setText("Floor Number : "+taskModel.getRoomFloor());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                key = taskModelLArrayList.keySet().toArray()[position].toString();
                AppConstants.taskModel = taskModelLArrayList.get(key);
                activity.startActivity(new Intent(activity, LostAndFoundItemUploadActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskModelLArrayList.size();
    }

}