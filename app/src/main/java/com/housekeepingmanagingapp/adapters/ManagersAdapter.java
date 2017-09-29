package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.AddandEditManagerActivity;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.models.ManagerModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class ManagersAdapter extends RecyclerView.Adapter<ManagersAdapter.MyViewHolder> {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private LinkedHashMap<String, ManagerModel> managerModelsList = new LinkedHashMap<>();
    Activity activity;
    @BindView(R.id.ivManagerImage)
    ImageView ivManagerImage;
    @BindView(R.id.tvManagerName)
    TextView tvManagerName;

    String key;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);


        }
    }

    public ManagersAdapter(Activity activity, LinkedHashMap<String, ManagerModel> managerModelsList) {

        this.activity = activity;
        this.managerModelsList = managerModelsList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_manager_item, parent, false);

        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            progressBar.setVisibility(View.VISIBLE);
            key = managerModelsList.keySet().toArray()[position].toString();
            tvManagerName.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

            tvManagerName.setText(managerModelsList.get(key).getFullName());

            //StorageReference imagePath = FirebaseStorage.getInstance().getReference().child("images").child(managerModelsList.get(key).getProfilePic());
//        Glide.with(activity)
//              //  .using(new FirebaseImageLoader())
//                .load(managerModelsList.get(key).getProfilePic())
//                .placeholder(R.drawable.loading)
//                .into(ivManagerImage);


            if (managerModelsList.get(key).getProfilePic().equals("")) {
                ivManagerImage.setImageResource(R.drawable.icon_user);
            } else {
                Picasso.with(activity)
                        .setIndicatorsEnabled(true);
                Picasso.with(activity)
                        .load(managerModelsList.get(key).getProfilePic())
                        .placeholder(R.drawable.loader)

                        .into(ivManagerImage, new Callback() {
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

                    Intent intent = new Intent(activity, AddandEditManagerActivity.class);
                    intent.putExtra("action", "edit");
                    // Passing current housekeeper model
                    key = managerModelsList.keySet().toArray()[position].toString();
                    intent.putExtra("managerModel", managerModelsList.get(key));
                    activity.startActivity(intent);


                }
            });
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return managerModelsList.size();
    }

}