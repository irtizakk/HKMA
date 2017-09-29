package com.housekeepingmanagingapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.dialogs.CustomAlertDialog;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.models.ManagerModel;
import com.housekeepingmanagingapp.models.UserModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.txtFullName)
    EditText txtFullName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtContactNumber)
    EditText txtContactNumber;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.tvPictureText)
    TextView tvPictureText;
    @BindView(R.id.btnUpdateProfile)
    Button btnUpdateProfile;

    UserModel userModel;
    ManagerModel managerModel;
    HouseKeeperModel houseKeeperModel;

    Activity activity;
    Uri imageUri;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.activity_addand_edit_house_keeper)
    RelativeLayout activityAddandEditHouseKeeper;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    private String currentDate;

    private int IMAGE_SELECT_PERMISSION = 100;

    public UserProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_transparent, menu);
        return;
    }


    private void selectUserImage(int a) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, a);
    }

    // --> Start setTypeFace() method.
    private void setTypeFace() {
        tvPictureText.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtContactNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

        txtFullName.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtPassword.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnUpdateProfile.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));

    }// <--End setTypeFace() method.
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        setTypeFace();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        currentDate = simpleDateFormat.format(date);

        userModel = AppController.getInstance().getPrefManger().getUserProfile();
        if (userModel.getRole().equals("manager"))
        {
            progressBar.setVisibility(View.VISIBLE);
            managerModel = AppController.getInstance().getPrefManger().getManagerProfile();
            txtContactNumber.setText(managerModel.getContactNumber());
            txtFullName.setText(managerModel.getFullName());
            ratingBar.setVisibility(View.GONE);
            Picasso.with(activity)
                    .setIndicatorsEnabled(true);
            Picasso.with(activity)
                    .load(managerModel.getProfilePic())
                    .placeholder(R.drawable.loader)
                    .into(ivUserImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError()
                        {

                        }
                    });
        }
        else if (userModel.getRole().equals("housekeeper"))
        {
            progressBar.setVisibility(View.VISIBLE);
            houseKeeperModel = AppController.getInstance().getPrefManger().getHouseKeeperProfile();
            txtContactNumber.setText(houseKeeperModel.getContactNumber());
            txtFullName.setText(houseKeeperModel.getFullName());
            ratingBar.setVisibility(View.VISIBLE);
            if(houseKeeperModel.getRating().toString().equals("0.0"))
            {
                ratingBar.setRating(Float.parseFloat(houseKeeperModel.getRating().toString()));
            }
            else if(houseKeeperModel.getRating().toString().contains(","))
            {
                String[] values = houseKeeperModel.getRating().toString().split(",");
                float totalRatings=0.0f;
                float ratingAverage = 0.0f;
                for(int i =0 ; i< values.length ; i++)
                {
                    totalRatings+= Float.parseFloat(values[i]);
                }

                ratingAverage = totalRatings/(values.length-1);
                ratingBar.setRating(Float.parseFloat(String.format("%.2f",ratingAverage)));
            }
            //ratingBar.setEnabled(false);
            ratingBar.setIsIndicator(true);
            Picasso.with(activity)
                    .load(houseKeeperModel.getProfilePic())
                    .placeholder(R.drawable.loader)
                    .into(ivUserImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError()
                        {

                        }
                    });
        }
    }
    private boolean checkValidation()
    {
        if (!AppConstants.isOnline(activity))
        {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtFullName.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Enter Correct Full Name", Toast.LENGTH_LONG).show();

            return false;
        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(txtContactNumber.getText().toString().trim())) {
            Toast.makeText(activity, "Please Enter Proper Phone Number", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @OnClick({R.id.ivUserImage, R.id.btnUpdateProfile})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivUserImage:
                selectUserImage(IMAGE_SELECT_PERMISSION);
                break;
            case R.id.btnUpdateProfile:
                // --> Start Main If block
                if (checkValidation())
                {
                    btnUpdateProfile.setClickable(false);
                    btnUpdateProfile.setText("Please Wait...");
                    // --> Start Image Uri checking If block
                    if (imageUri != null) {
                        // In this if we are updating user with his profile picture
                        spinKit.setVisibility(View.VISIBLE);
                        StorageReference imagePath = FirebaseStorage.getInstance().getReference().child("images").child(imageUri.getLastPathSegment());

                        imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadImageUrl = taskSnapshot.getDownloadUrl();

                                if (userModel.getRole().equals("manager")) {
                                    editManager(downloadImageUrl + "");
                                } else if (userModel.getRole().equals("housekeeper")) {
                                    editHouseKeeper(downloadImageUrl + "");
                                }
                            }
                        });
                    }// <-- End  Image Uri checking If block
                    else {
                        // In this else we are updating user except his profile picture
                        spinKit.setVisibility(View.VISIBLE);
                        if (userModel.getRole().equals("manager")) {
                            editManager("");
                        } else if (userModel.getRole().equals("housekeeper")) {
                            editHouseKeeper("");
                        }
                    }
                }//  <-- End Main If block
                break;
        }
    }

    private void editManager(String downloadImageUrl) {

        String managerId = managerModel.getUserId();


        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        DatabaseReference database = firebaseFactory.getDatabaseRef().child("manager").child(managerId);
        database.child("fullName").setValue(txtFullName.getText().toString());

        if (txtPassword.getText().toString().length() > 0) {
            database.child("password").setValue(txtPassword.getText().toString());
            DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("users").child(managerId);
            database2.child("password").setValue(txtPassword.getText().toString());
        }
        if (imageUri != null) {
            managerModel.setProfilePic(downloadImageUrl + "");
            database.child("profilePic").setValue(downloadImageUrl + "");
        }

        database.child("contactNumber").setValue(txtContactNumber.getText().toString());
        database.child("date").setValue(currentDate);
        spinKit.setVisibility(View.GONE);

        managerModel.setFullName(txtFullName.getText().toString());
        managerModel.setContactNumber(txtContactNumber.getText().toString());

        AppController.getInstance().getPrefManger().setManagerProfile(managerModel);
        new CustomAlertDialog(activity, "Alert Update", "Successfully Updated Profile");
        btnUpdateProfile.setClickable(true);
        btnUpdateProfile.setText("UPDATE PROFILE");
    }

    private void editHouseKeeper(String downloadImageUrl) {
        String houseKeeperId = houseKeeperModel.getUserId();


        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        DatabaseReference database = firebaseFactory.getDatabaseRef().child("houseKeeper").child(houseKeeperId);
        database.child("fullName").setValue(txtFullName.getText().toString());

        houseKeeperModel.setFullName(txtFullName.getText().toString());
        houseKeeperModel.setContactNumber(txtContactNumber.getText().toString());
        if (txtPassword.getText().toString().length() > 0) {
            database.child("password").setValue(txtPassword.getText().toString());
            DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("users").child(houseKeeperId);
            database2.child("password").setValue(txtPassword.getText().toString());
        }

        if (imageUri != null) {
            houseKeeperModel.setProfilePic(downloadImageUrl + "");
            database.child("profilePic").setValue(downloadImageUrl + "");
        }

        database.child("contactNumber").setValue(txtContactNumber.getText().toString());
        database.child("date").setValue(currentDate);
        spinKit.setVisibility(View.GONE);

        new CustomAlertDialog(activity, "Alert Update", "Successfully Updated Profile");

        AppController.getInstance().getPrefManger().setHousekeeperProfile(houseKeeperModel);
        btnUpdateProfile.setClickable(true);
        btnUpdateProfile.setText("UPDATE PROFILE");
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == IMAGE_SELECT_PERMISSION && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(activity, this);
        }

        if (reqCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                ivUserImage.setImageURI(imageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }
}
