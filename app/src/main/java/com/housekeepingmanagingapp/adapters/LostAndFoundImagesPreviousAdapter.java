package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.housekeepingmanagingapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class LostAndFoundImagesPreviousAdapter extends RecyclerView.Adapter<LostAndFoundImagesPreviousAdapter.MyViewHolder> {

    Activity activity;


    private ArrayList<String> lostAndFoundPreviousImages = new ArrayList<>();




    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public LostAndFoundImagesPreviousAdapter(Activity activity, ArrayList<String> lostAndFoundItemsImages) {

        this.activity = activity;
        this.lostAndFoundPreviousImages = lostAndFoundItemsImages;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_current_images_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(activity)
                .setIndicatorsEnabled(true);
        Picasso.with(activity)
                .load(lostAndFoundPreviousImages.get(position))
                .placeholder(R.drawable.loader)
                .into(holder.ivImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return lostAndFoundPreviousImages.size();
    }

}