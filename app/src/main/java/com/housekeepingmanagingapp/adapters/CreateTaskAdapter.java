package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.housekeepingmanagingapp.R;

import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class CreateTaskAdapter extends RecyclerView.Adapter<CreateTaskAdapter.MyViewHolder> {

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);


        }
    }

    public CreateTaskAdapter(Activity activity) {

        this.activity = activity;



    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_create_task_item, parent, false);

        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {




    }

    @Override
    public int getItemCount() {
        return 10;
    }

}