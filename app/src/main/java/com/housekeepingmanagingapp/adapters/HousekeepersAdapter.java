package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.ActivityChat;
import com.housekeepingmanagingapp.activity.AddandEditHouseKeeperActivity;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 1/31/2017.
 */
public class HousekeepersAdapter extends RecyclerView.Adapter<HousekeepersAdapter.MyViewHolder> {
    @BindView(R.id.ivhouseKeeperImage)
    CircleImageView ivhouseKeeperImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private LinkedHashMap<String, HouseKeeperModel> houseKeeperModelLArrayList = new LinkedHashMap<>();
    Activity activity;
    @BindView(R.id.tvUserName)
    TextView tvUserName;

    private String key;

    // Who is going to see this screen
    // and what to do on click
    private String choice = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);

        }
    }

    public HousekeepersAdapter(Activity activity, LinkedHashMap<String, HouseKeeperModel> houseKeeperModelLArrayList, String choice) {

        this.activity = activity;
        this.houseKeeperModelLArrayList = houseKeeperModelLArrayList;
        this.choice = choice;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_housekeeper_item, parent, false);

        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        try {
            progressBar.setVisibility(View.VISIBLE);
            key = houseKeeperModelLArrayList.keySet().toArray()[position].toString();
            tvUserName.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
            tvUserName.setText(houseKeeperModelLArrayList.get(key).getFullName());

            if(houseKeeperModelLArrayList.get(key).getRating().toString().equals("0.0"))
            {
                ratingBar.setRating(Float.parseFloat(houseKeeperModelLArrayList.get(key).getRating().toString()));
            }
            else if(houseKeeperModelLArrayList.get(key).getRating().toString().contains(","))
            {
                String[] values = houseKeeperModelLArrayList.get(key).getRating().toString().split(",");
                float totalRatings=0.0f;
                float ratingAverage = 0.0f;
                for(int i =0 ; i< values.length ; i++)
                {
                   totalRatings+= Float.parseFloat(values[i]);
                }

                ratingAverage = totalRatings/(values.length-1);
                ratingBar.setRating(Float.parseFloat(String.format("%.2f",ratingAverage)));
            }




            if (houseKeeperModelLArrayList.get(key).getProfilePic().equals("")) {
                ivhouseKeeperImage.setImageResource(R.drawable.icon_user);
            } else {
                Picasso.with(activity)
                        .setIndicatorsEnabled(true);
                Picasso.with(activity)
                        .load(houseKeeperModelLArrayList.get(key).getProfilePic())
                        .placeholder(R.drawable.loader)
                        .into(ivhouseKeeperImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    key = houseKeeperModelLArrayList.keySet().toArray()[position].toString();

                    if (choice.equals("adminView") || choice.equals("managerDetailView")) {
                        Intent intent = new Intent(activity, AddandEditHouseKeeperActivity.class);
                        intent.putExtra("action", "edit");
                        intent.putExtra("houseKeeperModel", houseKeeperModelLArrayList.get(key));
                        intent.putExtra("choice", choice);
                        activity.startActivity(intent);
                    } else if (choice.equals("managerSelectHouseKeeperForMessage")) {

                        AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_ID, houseKeeperModelLArrayList.get(key).getUserId());
                        AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_NAME, houseKeeperModelLArrayList.get(key).getFullName());
                        AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_IMAGE, houseKeeperModelLArrayList.get(key).getProfilePic());

                        activity.startActivity(new Intent(activity, ActivityChat.class));
                        activity.finish();
                    } else if (choice.equals("managerSelectHouseKeeperForTask")) {

                        AppController.getInstance().getPrefManger().setHousekeeperProfile(houseKeeperModelLArrayList.get(key));

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
        return houseKeeperModelLArrayList.size();
    }

}