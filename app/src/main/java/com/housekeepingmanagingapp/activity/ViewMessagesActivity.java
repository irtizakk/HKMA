package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.adapters.ChatMembersViewAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.fragments.ViewHouseKeepersFragment;
import com.housekeepingmanagingapp.models.ChatModel;
import com.housekeepingmanagingapp.models.UserModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewMessagesActivity extends AppCompatActivity {

    @BindView(R.id.rvAllMessages)
    ShimmerRecyclerView rvAllMessages;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivNewMessages)
    ImageView ivNewMessages;


    Activity activity;
    @BindView(R.id.ivError)
    ImageView ivError;
    private String action = "";
    ChatMembersViewAdapter chatMembersViewAdapter;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        ButterKnife.bind(this);

        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
        if (userModel.getRole().equals("manager")) {
            action = "managerSelectHouseKeeperForMessage";

        } else if (userModel.getRole().equals("housekeeper")) {
            ivNewMessages.setVisibility(View.GONE);
        }


        spinKit.setVisibility(View.VISIBLE);

        changeActionBarText("Messages");

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllMessages.setLayoutManager(layoutManager);

        getAllMessages();


    }

    private void getAllMessages()
    {
        Log.e("test","message called");
        FirebaseFactory.getFirebaseFactory().getDatabaseRef().child("chatUId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.e("test","simple child");
                if (dataSnapshot.hasChildren())
                {
                    Log.e("test","have child");
                    chatModelArrayList.clear();
                    GenericTypeIndicator<LinkedHashMap<String, ChatModel>> genericTypeIndicator = new GenericTypeIndicator<LinkedHashMap<String, ChatModel>>() {
                    };
                    LinkedHashMap<String, ChatModel> allChatValues = new LinkedHashMap<>();
                    allChatValues.putAll(dataSnapshot.getValue(genericTypeIndicator));


                    //--> Start getting all users Ids from whome current user chat
                    for (int count = 0; count < allChatValues.size(); count++)
                    {
                        String hashKey = allChatValues.keySet().toArray()[count].toString();

                        ChatModel chatModel = allChatValues.get(hashKey);
                        // sender and reciver id
                        String sId = chatModel.getSenderId();
                        String rId = chatModel.getRecieverId();


                        if ((sId.equals(AppController.getInstance().getPrefManger().getUserProfile().getUserId()))) {
                            chatModelArrayList.add(chatModel);

                        }
                        else if (rId.equals(AppController.getInstance().getPrefManger().getUserProfile().getUserId())) {
                            chatModelArrayList.add(chatModel);

                        }

                        if(count == allChatValues.size() -1)
                        {
                            if(chatModelArrayList.size() == 0)
                            {
                                Log.e("test","error child");
                                ivError.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, "No Message Available", Toast.LENGTH_SHORT).show();
                                rvAllMessages.setVisibility(View.GONE);
                            }
                            else
                            {
                                chatMembersViewAdapter = new ChatMembersViewAdapter(activity, chatModelArrayList);
                                rvAllMessages.setAdapter(chatMembersViewAdapter);
                                spinKit.setVisibility(View.GONE);
                                ivError.setVisibility(View.GONE);
                                rvAllMessages.setVisibility(View.VISIBLE);
                            }
                        }

                    }//<-- End getting all users Ids from whome current user chat

                }
                else
                {
                    Log.e("test","error child");
                    ivError.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "No Message Available", Toast.LENGTH_SHORT).show();
                    rvAllMessages.setVisibility(View.GONE);
                }


            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeActionBarText(String text) {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    @OnClick(R.id.ivNewMessages)
    public void onClick() {
        // we are passing here "managerSelectHouseKeeper" as a value of choice in  viewHouseKeepersFragment
        // we pass this value so that manager select the house keeper and move to Activity Chat.
        ViewHouseKeepersFragment viewHouseKeepersFragment = ViewHouseKeepersFragment.newInstance(action);
        FragmentOpenerActivity.newInstance(viewHouseKeepersFragment, "Select HouseKeeper");
        startActivity(new Intent(activity, FragmentOpenerActivity.class));

    }
}
