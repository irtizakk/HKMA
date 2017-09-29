package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.housekeepingmanagingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class LostAndFoundImagesCurrentAdapter extends RecyclerView.Adapter<LostAndFoundImagesCurrentAdapter.MyViewHolder> {

    Activity activity;

    private ArrayList<Uri> lostAndFoundCurrentImages = new ArrayList<>();




    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }

    public LostAndFoundImagesCurrentAdapter(Activity activity, ArrayList<Uri> lostAndFoundCurrentImages) {

        this.activity = activity;
        this.lostAndFoundCurrentImages = lostAndFoundCurrentImages;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_current_images_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.ivImage.setImageURI(lostAndFoundCurrentImages.get(position));
    }

    @Override
    public int getItemCount() {
        return lostAndFoundCurrentImages.size();
    }

}