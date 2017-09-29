package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.adapters.ViewLostAndFoundItemsAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.models.TaskModel;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LostAndFountItemViewActivity extends AppCompatActivity {

    @BindView(R.id.rvAllLostAndFoundItems)
    ShimmerRecyclerView rvAllLostAndFoundItems;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivError)
    ImageView ivError;
    private LinkedHashMap<String, TaskModel> taskModelLArrayList = new LinkedHashMap<>();

    private Activity activity;
    ViewLostAndFoundItemsAdapter adapter;
     boolean foundItems = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_fount_item_view);
        ButterKnife.bind(this);
        activity = this;

        changeActionBarText("Lost And Found Items");

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllLostAndFoundItems.setLayoutManager(layoutManager);
        adapter = new ViewLostAndFoundItemsAdapter(activity, taskModelLArrayList);
        rvAllLostAndFoundItems.setAdapter(adapter);
        getAllTask();
    }

    public void changeActionBarText(String text)
    {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    // getting all housekeepers from firebase database who are going in the event
    private void getAllTask()
    {

        spinKit.setVisibility(View.VISIBLE);
        ivError.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("tasks")
                .orderByChild("managerId").equalTo(AppController.getInstance().getPrefManger().getManagerProfile().getUserId());
        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                taskModelLArrayList.clear();

                if(dataSnapshot.hasChildren())
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        TaskModel taskModel = dataSnapshot1.getValue(TaskModel.class);

                        if (taskModel.getLostAndFoundItemsImages().size() > 0)
                        {
                            spinKit.setVisibility(View.GONE);
                            taskModelLArrayList.put(dataSnapshot.getKey(), taskModel);
                            foundItems = true;

                            adapter.notifyDataSetChanged();
                        }
                        else if (foundItems == true)
                        {
                            ivError.setVisibility(View.GONE);
                            rvAllLostAndFoundItems.setVisibility(View.VISIBLE);

                        }

                    }
                }
                else
                {
                    spinKit.setVisibility(View.GONE);
                    ivError.setVisibility(View.VISIBLE);
                    rvAllLostAndFoundItems.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                spinKit.setVisibility(View.GONE);
            }
        });
    }
}
