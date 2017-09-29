package com.housekeepingmanagingapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.housekeepingmanagingapp.activity.AddandEditHouseKeeperActivity;
import com.housekeepingmanagingapp.adapters.HousekeepersAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.models.UserModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewHouseKeepersFragment extends Fragment
{


    @BindView(R.id.rvAllHousekeepers)
    ShimmerRecyclerView rvAllHousekeepers;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    Activity activity;
    @BindView(R.id.ivAddHouseKeeper)
    ImageView ivAddHouseKeeper;
    @BindView(R.id.ivError)
    ImageView ivError;
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    private LinkedHashMap<String, HouseKeeperModel> houseKeeperModelLArrayList = new LinkedHashMap<>();
    private LinkedHashMap<String, HouseKeeperModel> searchAllhouseKeeperModelLArrayList = new LinkedHashMap<>();
    HousekeepersAdapter housekeepersAdapter;

    private  boolean showSearchField;

    private static String choice = "";
    public ViewHouseKeepersFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_house_keepers, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.after_login_admin, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_search:

                 if(!showSearchField)
                 {
                     search();
                     txtSearch.setVisibility(View.VISIBLE);
                     showSearchField = true;
                 }
                else
                 {
                     search();
                     txtSearch.setVisibility(View.GONE);
                     showSearchField = false;
                 }
                return true;

        }

        return false;
    }



    private void initialize()
    {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        rvAllHousekeepers.setLayoutManager(layoutManager);



        rvAllHousekeepers.setHasFixedSize(true);
         rvAllHousekeepers.setItemViewCacheSize(20);
        rvAllHousekeepers.setDrawingCacheEnabled(true);

        getAllHouseKeepers();

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
                searchAllhouseKeeperModelLArrayList.clear();


                for (int i = 0; i < houseKeeperModelLArrayList.size(); i++)
                {
                    String key = houseKeeperModelLArrayList.keySet().toArray()[i] + "";

                    if (houseKeeperModelLArrayList.get(key).getFullName().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchAllhouseKeeperModelLArrayList.put(key, houseKeeperModelLArrayList.get(key));

                    }
                }

                rvAllHousekeepers.removeAllViewsInLayout();

                housekeepersAdapter = new HousekeepersAdapter(activity, searchAllhouseKeeperModelLArrayList,choice);
                rvAllHousekeepers.setAdapter(housekeepersAdapter);


            }
        });
    }

    // getting all housekeepers from firebase database who are going in the event
    private void getAllHouseKeepers()
    {
        try {

            spinKit.setVisibility(View.VISIBLE);

            Query query = FirebaseDatabase.getInstance().getReference("housekeeper")
                    .orderByChild("locationId").equalTo(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, "").toString());
            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    houseKeeperModelLArrayList.clear();
                    searchAllhouseKeeperModelLArrayList.clear();
                    if (dataSnapshot.hasChildren())
                    {
                        ivError.setVisibility(View.GONE);
                        rvAllHousekeepers.setVisibility(View.VISIBLE);
                        GenericTypeIndicator<HashMap<String, HouseKeeperModel>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, HouseKeeperModel>>() {
                        };

                        houseKeeperModelLArrayList.putAll(dataSnapshot.getValue(genericTypeIndicator));


                        housekeepersAdapter = new HousekeepersAdapter(getActivity(), houseKeeperModelLArrayList, choice);
                        rvAllHousekeepers.setAdapter(housekeepersAdapter);

                        spinKit.setVisibility(View.GONE);
                    }
                    else
                    {

                        Toast.makeText(activity, "No HouseKeepers Available In This Location", Toast.LENGTH_LONG).show();
                        ivError.setVisibility(View.VISIBLE);
                        rvAllHousekeepers.setVisibility(View.GONE);
                        spinKit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    spinKit.setVisibility(View.GONE);
                }
            });
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();


        spinKit.setVisibility(View.VISIBLE);

        initialize();

        UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
        if(userModel.getRole().equals("manager"))
        {
            ivAddHouseKeeper.setVisibility(View.GONE);

        }

    }

    public  static ViewHouseKeepersFragment newInstance(String data)
    {

        ViewHouseKeepersFragment fragment = new ViewHouseKeepersFragment();
         choice = data;
        return fragment;
    }

    @OnClick(R.id.ivAddHouseKeeper)
    public void onClick()
    {
        Intent intent = new Intent(activity, AddandEditHouseKeeperActivity.class);
        intent.putExtra("action", "add");
        intent.putExtra("choice", "adminView");
        activity.startActivity(intent);
    }
}
