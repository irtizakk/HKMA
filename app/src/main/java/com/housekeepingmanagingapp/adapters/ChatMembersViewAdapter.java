package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.ActivityChat;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.ChatModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class ChatMembersViewAdapter extends RecyclerView.Adapter<ChatMembersViewAdapter.MyViewHolder> {



    private ArrayList<ChatModel> chatModelArrayList = null;
    private Activity activity;
    private String key;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.ivUserImage)
        ImageView ivUserImage;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.liAssignTasks)
        LinearLayout liAssignTasks;
        @BindView(R.id.tvMessage)
        TextView tvMessage;


        public MyViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ChatMembersViewAdapter(Activity activity, ArrayList<ChatModel> chatModelArrayList) {
        this.chatModelArrayList = chatModelArrayList;
        this.activity = activity;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_message_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {


        holder.progressBar.setVisibility(View.VISIBLE);

        if(chatModelArrayList.get(position).getText().toString().length() > 30)
        {
            holder.tvMessage.setText(chatModelArrayList.get(position).getText().substring(0,25)+"....");
        }
        else
        {
            holder.tvMessage.setText(chatModelArrayList.get(position).getText());
        }

        String date = chatModelArrayList.get(position).getDate().substring(0, chatModelArrayList.get(position).getDate().indexOf(" "));
        String time = chatModelArrayList.get(position).getDate().substring(chatModelArrayList.get(position).getDate().indexOf(" ") + 1);
        holder.tvTime.setText(date + "\n" + time);
        if (chatModelArrayList.get(position).getRecieverId().equals(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID, ""))) {
            holder.tvUserName.setText(chatModelArrayList.get(position).getSenderName());
            Picasso.with(activity)
                    .load(chatModelArrayList.get(position).getSenderImage())
                    .placeholder(R.drawable.loader)

                    .into(holder.ivUserImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else if (chatModelArrayList.get(position).getSenderId().equals(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID, ""))) {
            holder.tvUserName.setText(chatModelArrayList.get(position).getRecieverName());
            Picasso.with(activity)
                    .load(chatModelArrayList.get(position).getRecieverImage())
                    .placeholder(R.drawable.loader)

                    .into(holder.ivUserImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError()
                        {

                        }
                    });
        }


        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if (chatModelArrayList.get(position).getRecieverId().equals(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID, "")))
                {

                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_ID,chatModelArrayList.get(position).getSenderId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_NAME, chatModelArrayList.get(position).getSenderName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_IMAGE, chatModelArrayList.get(position).getSenderImage());
                }
                else if (chatModelArrayList.get(position).getSenderId().equals(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID, "")))
                {
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_ID, chatModelArrayList.get(position).getRecieverId());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_NAME, chatModelArrayList.get(position).getRecieverName());
                    AppController.getInstance().getPrefManger().setValue(PrefManager.KEY_RECEIVER_IMAGE, chatModelArrayList.get(position).getRecieverImage());
                }

                activity.startActivity(new Intent(activity, ActivityChat.class));


            }
        });


    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }


}
