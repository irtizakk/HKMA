package com.housekeepingmanagingapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.housekeepingmanagingapp.activity.AddandEditManagerActivity;
import com.housekeepingmanagingapp.adapters.ManagersAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.ManagerModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewManagersFragment extends Fragment {


    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    Activity activity;

    @BindView(R.id.rvAllManagers)
    ShimmerRecyclerView rvAllManagers;
    @BindView(R.id.ivAddManager)
    ImageView ivAddManager;
    @BindView(R.id.ivError)
    ImageView ivError;

    ManagersAdapter managersAdapter;
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    private LinkedHashMap<String, ManagerModel> managerModelsList = new LinkedHashMap<>();
    private LinkedHashMap<String, ManagerModel> searchAllManagerModelsList = new LinkedHashMap<>();
    public ViewManagersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_managers, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        rvAllManagers.setLayoutManager(layoutManager);

        rvAllManagers.setHasFixedSize(true);
        rvAllManagers.setItemViewCacheSize(20);
        rvAllManagers.setDrawingCacheEnabled(true);

        getAllManagers();

        search();
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
                if(txtSearch.getVisibility() ==  View.GONE)
                {
                    txtSearch.setVisibility(View.VISIBLE);

                }
                else
                {
                    txtSearch.setVisibility(View.GONE);

                }
                return true;


        }

        return false;
    }

    @OnClick(R.id.ivAddManager)
    public void onClick() {
        Intent intent = new Intent(activity, AddandEditManagerActivity.class);
        intent.putExtra("action", "add");
        activity.startActivity(intent);
    }


    private void search() {
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
                searchAllManagerModelsList.clear();


                for (int i = 0; i < managerModelsList.size(); i++) {
                    String key = managerModelsList.keySet().toArray()[i] + "";

                    if (managerModelsList.get(key).getFullName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchAllManagerModelsList.put(key, managerModelsList.get(key));

                    }
                }

                rvAllManagers.removeAllViewsInLayout();

                managersAdapter = new ManagersAdapter(activity, searchAllManagerModelsList);
                rvAllManagers.setAdapter(managersAdapter);


            }
        });
    }

    // getting all housekeepers from firebase database who are going in the event
    private void getAllManagers()
    {

        try
        {



        spinKit.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("manager")
                .orderByChild("locationId").equalTo(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, "").toString());


        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                managerModelsList.clear();

                if (dataSnapshot.hasChildren())
                {
                    Log.e("test", "Called me");
                    ivError.setVisibility(View.GONE);
                    rvAllManagers.setVisibility(View.VISIBLE);
                    GenericTypeIndicator<HashMap<String, ManagerModel>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, ManagerModel>>() {
                    };

                    managerModelsList.putAll(dataSnapshot.getValue(genericTypeIndicator));

                    managersAdapter = new ManagersAdapter(activity, managerModelsList);
                    rvAllManagers.setAdapter(managersAdapter);


                    spinKit.setVisibility(View.GONE);
                }
                else
                {

                    Toast.makeText(activity, "No Managers Available In This Location", Toast.LENGTH_LONG).show();
                    ivError.setVisibility(View.VISIBLE);
                    rvAllManagers.setVisibility(View.GONE);
                    spinKit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                spinKit.setVisibility(View.GONE);
            }
        });
        }
        catch (Exception e)
        {

        }
    }
}
