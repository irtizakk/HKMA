package com.housekeepingmanagingapp.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.MessageModel;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.MyViewHolder> {

    private LinkedHashMap<String, MessageModel> allMessagesList = null;
    private Activity activity;
    private String key;



    public static int position;



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvTextMessage)
        TextView tvTextMessage;
        @BindView(R.id.tvMessageTime)
        TextView tvMessageTime;
        @BindView(R.id.ivMessageImage)
        ImageView ivMessageImage;

        public MyViewHolder(View v)
        {
            super(v);

            ButterKnife.bind(this,v);

        }
    }

    public MessageDataAdapter(Activity activity, LinkedHashMap<String, MessageModel> allMessagesList) {
        this.allMessagesList = allMessagesList;
        this.activity = activity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_out_message, parent, false);

        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_in_message, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        key = allMessagesList.keySet().toArray()[position].toString();

        if (holder.getItemViewType() == 2)
        {
            holder.tvUserName.setText(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_NAME,"").toString());
        }
        else
        {
            holder.tvUserName.setText(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_NAME,"").toString());
        }

        holder.tvMessageTime.setText(allMessagesList.get(key).getDate());
        holder. tvTextMessage.setText(allMessagesList.get(key).getText());

        holder.ivMessageImage.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount()
    {
        return allMessagesList.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        super.getItemViewType(position);
        key = allMessagesList.keySet().toArray()[position].toString();
        MessageModel chatMessage = allMessagesList.get(key);
        if (AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID,"").equals(chatMessage.getSenderId()))
            return 1;
        else
            return 2;
    }


//    private void showZoomImageDialog(String image) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_image_zoom);
//
//        ImageView ivImageZoom = (ImageView) dialog.findViewById(R.id.ivImageZoom);
//        // Picasso.with(context).setIndicatorsEnabled(true);
//        Picasso.with(context).load(image).placeholder(R.drawable.small_image_loading).into(ivImageZoom);
//
//        dialog.getWindow().getAttributes().windowAnimations = R.style.customDialog;
//        dialog.show();
//
//    }
}
