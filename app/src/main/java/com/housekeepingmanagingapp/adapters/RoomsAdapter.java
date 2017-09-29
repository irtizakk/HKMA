package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.AddandEditRoomActivity;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.models.RoomModel;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {
    private LinkedHashMap<String, RoomModel> roomModelsList = new LinkedHashMap<>();
    Activity activity;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvFloor)
    TextView tvFloor;

    private String key,choice;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);


        }
    }

    public RoomsAdapter(Activity activity, LinkedHashMap<String, RoomModel> roomModelsList,String choice ) {

        this.activity = activity;
        this.roomModelsList = roomModelsList;
        this.choice=choice;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_room_item, parent, false);

        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        try {
            key = roomModelsList.keySet().toArray()[position].toString();
            tvRoom.setText(roomModelsList.get(key).getRoomNumber());
            tvFloor.setText(roomModelsList.get(key).getRoomFloorNumber());

            tvRoom.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
            tvFloor.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    key = roomModelsList.keySet().toArray()[position].toString();
                    if (choice.equals("adminView") || choice.equals("managerDetailView")) {
                        Intent intent = new Intent(activity, AddandEditRoomActivity.class);
                        intent.putExtra("action", "edit");
                        // Passing current housekeeper model

                        intent.putExtra("roomModel", roomModelsList.get(key));
                        intent.putExtra("choice", choice);
                        activity.startActivity(intent);
                    }
                    if (choice.equals("managerSelectRoomForTask")) {
                        AppController.getInstance().getPrefManger().setRoomModel(roomModelsList.get(key));
                        activity.finish();

                    }


                }
            });
        }
        catch (Exception e)
        {

        }
    }


    @Override
    public int getItemCount() {
        return roomModelsList.size();
    }

}