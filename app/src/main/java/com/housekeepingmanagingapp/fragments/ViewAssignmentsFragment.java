package com.housekeepingmanagingapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.activity.CreateAssignmentActivity;
import com.housekeepingmanagingapp.adapters.AssignmentsAdapter;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.models.TaskModel;
import com.housekeepingmanagingapp.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAssignmentsFragment extends Fragment {


    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    Activity activity;
    ArrayList<String> assignmentsModelArrayList = new ArrayList<>();

    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvHousekeepr)
    TextView tvHousekeepr;
    @BindView(R.id.rvAllAssignments)
    ShimmerRecyclerView rvAllAssignments;
    @BindView(R.id.ivAddTask)
    ImageView ivAddTask;
    @BindView(R.id.ivError)
    ImageView ivError;
    UserModel userModel;
    @BindView(R.id.txtSearch)
    EditText txtSearch;

    private LinkedHashMap<String, TaskModel> taskModelLArrayList = new LinkedHashMap<>();
    private LinkedHashMap<String, TaskModel> searchAllTaskModelLArrayList = new LinkedHashMap<>();
    AssignmentsAdapter adapter;

    private String taskUserId, taskUserType;

    public ViewAssignmentsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_assignments, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }
    private void search()
    {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                searchAllTaskModelLArrayList.clear();


                for (int i = 0; i < taskModelLArrayList.size(); i++)
                {
                    String key = taskModelLArrayList.keySet().toArray()[i] + "";

                    if (taskModelLArrayList.get(key).getRoomNo().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchAllTaskModelLArrayList.put(key, taskModelLArrayList.get(key));

                    }
                }

                rvAllAssignments.removeAllViewsInLayout();

                adapter = new AssignmentsAdapter(activity, searchAllTaskModelLArrayList);
                rvAllAssignments.setAdapter(adapter);


            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.after_login_admin, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:

                if(txtSearch.getVisibility() == View.VISIBLE)
                {
                    txtSearch.setVisibility(View.GONE);
                }
                else if(txtSearch.getVisibility() == View.GONE)
                {
                    txtSearch.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        spinKit.setVisibility(View.VISIBLE);

        userModel = AppController.getInstance().getPrefManger().getUserProfile();

        if (userModel.getRole().equals("housekeeper")) {
            ivAddTask.setVisibility(View.GONE);

            tvHousekeepr.setText("Assigned By Manager");
        }
        taskUserId = userModel.getUserId();
        taskUserType = userModel.getRole() + "Id";

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        rvAllAssignments.setLayoutManager(layoutManager);

        getAllTaskById();

        search();
    }

    // getting all housekeepers from firebase database who are going in the event
    private void getAllTaskById()
    {

        spinKit.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("tasks")
                .orderByChild(taskUserType).equalTo(taskUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                taskModelLArrayList.clear();
                searchAllTaskModelLArrayList.clear();
                if (dataSnapshot.hasChildren()) {
                    ivError.setVisibility(View.GONE);
                    rvAllAssignments.setVisibility(View.VISIBLE);
                    GenericTypeIndicator<HashMap<String, TaskModel>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, TaskModel>>() {
                    };

                    taskModelLArrayList.putAll(dataSnapshot.getValue(genericTypeIndicator));

                    notifyToSubscribers(AppConstants.ON_TASK_UPDATE);

                    Log.e("test", "taskModelLArrayList " + taskModelLArrayList.size());

                    adapter = new AssignmentsAdapter(getActivity(), taskModelLArrayList);
                    rvAllAssignments.setAdapter(adapter);

                    spinKit.setVisibility(View.GONE);
                } else {

                    Toast.makeText(activity, "No Task Available ", Toast.LENGTH_LONG).show();
                    ivError.setVisibility(View.VISIBLE);
                    rvAllAssignments.setVisibility(View.GONE);
                    spinKit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                spinKit.setVisibility(View.GONE);
            }
        });
    }
    private void notifyToSubscribers(String action)
    {
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("onUpdate").putExtra("action", action));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("onUpdateManager").putExtra("action", action));
    }

    @OnClick(R.id.ivAddTask)
    public void onClick() {
        Intent intent = new Intent(activity, CreateAssignmentActivity.class);
        intent.putExtra("action", "add");
        activity.startActivity(intent);
    }


}
