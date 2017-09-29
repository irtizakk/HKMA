package com.housekeepingmanagingapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.housekeepingmanagingapp.activity.AddandEditRoomActivity;
import com.housekeepingmanagingapp.adapters.RoomsAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.RoomModel;
import com.housekeepingmanagingapp.models.UserModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRoomsFragment extends Fragment {


    Activity activity;
    @BindView(R.id.rvAllRooms)
    ShimmerRecyclerView rvAllRooms;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivAddRoom)
    ImageView ivAddRoom;

    RoomsAdapter adapter;
    @BindView(R.id.ivError)
    ImageView ivError;
    @BindView(R.id.txtSearch)
    EditText txtSearch;

    private LinkedHashMap<String, RoomModel> roomModelsList = new LinkedHashMap<>();
    private LinkedHashMap<String, RoomModel> searchAllRoomModelsList= new LinkedHashMap<>();

    private static String choice = "";

    public ViewRoomsFragment() {
        // Required empty public constructor
    }


    private void initialize() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        rvAllRooms.setLayoutManager(layoutManager);


        rvAllRooms.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvAllRooms.setHasFixedSize(true);
        rvAllRooms.setItemViewCacheSize(20);
        rvAllRooms.setDrawingCacheEnabled(true);

        getAllRooms();
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
            public void afterTextChanged(Editable s) {
                searchAllRoomModelsList.clear();


                for (int i = 0; i < roomModelsList.size(); i++) {
                    String key = roomModelsList.keySet().toArray()[i] + "";

                    if (roomModelsList.get(key).getRoomNumber().contains(s.toString().toLowerCase())) {
                        searchAllRoomModelsList.put(key, roomModelsList.get(key));

                    }
                }

                rvAllRooms.removeAllViewsInLayout();

                adapter = new RoomsAdapter(activity, searchAllRoomModelsList,choice);
                rvAllRooms.setAdapter(adapter);


            }
        });
    }

    // getting all housekeepers from firebase database who are going in the event
    private void getAllRooms()
    {

        spinKit.setVisibility(View.VISIBLE);

        Query query = FirebaseDatabase.getInstance().getReference("rooms")
                .orderByChild("locationId").equalTo(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, "").toString());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                roomModelsList.clear();
                if (dataSnapshot.hasChildren()) {
                    ivError.setVisibility(View.GONE);
                    rvAllRooms.setVisibility(View.VISIBLE);
                    GenericTypeIndicator<HashMap<String, RoomModel>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, RoomModel>>() {
                    };

                    roomModelsList.putAll(dataSnapshot.getValue(genericTypeIndicator));

                    adapter = new RoomsAdapter(getActivity(), roomModelsList,choice);
                    rvAllRooms.setAdapter(adapter);


                    spinKit.setVisibility(View.GONE);
                } else {

                    Toast.makeText(activity, "No Rooms Available In This Location", Toast.LENGTH_LONG).show();
                    ivError.setVisibility(View.VISIBLE);
                    rvAllRooms.setVisibility(View.GONE);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_rooms, container, false);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        initialize();

        search();

        UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
        if(userModel.getRole().equals("manager"))
        {
            ivAddRoom.setVisibility(View.GONE);

        }

    }

    public  static ViewRoomsFragment newInstance(String data)
    {
        ViewRoomsFragment fragment = new ViewRoomsFragment();
        choice = data;
        return fragment;
    }

    @OnClick(R.id.ivAddRoom)
    public void onClick()
    {
        Intent intent = new Intent(activity, AddandEditRoomActivity.class);
        intent.putExtra("action", "add");
        intent.putExtra("choice", "adminView");
        activity.startActivity(intent);
    }
}
