package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.dialogs.CustomAlertDialog;
import com.housekeepingmanagingapp.models.HouseKeeperModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddandEditHouseKeeperActivity extends AppCompatActivity {
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.tvPictureText)
    TextView tvPictureText;
    @BindView(R.id.txtFullName)
    EditText txtFullName;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtContactNumber)
    EditText txtContactNumber;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.rlEditAndDelete)
    RelativeLayout rlEditAndDelete;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String action = "", choice = "", currentDate;
    ;

    Activity activity;
    Uri imageUri;
    private String previousContactNumber;

    HouseKeeperModel houseKeeperModel;
    Bundle bundle;

    private int IMAGE_SELECT_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addand_edit_house_keeper);
        ButterKnife.bind(this);

        activity = this;

        setTypeFace();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle  = getIntent().getExtras();
        if (bundle != null)
        {
            choice = bundle.getString("choice");
            if (choice.equals("adminView"))
            {
                action = bundle.getString("action");

                if (action.equals("edit"))
                {
                    getHouseKeeperData();
                    btnSave.setVisibility(View.GONE);
                    txtEmail.setVisibility(View.GONE);
                    tvPictureText.setText("Click On Image To Change/Update Image");

                    changeActionBarText("Edit HouseKeeper");

                }
                else if (action.equals("add"))
                {
                    rlEditAndDelete.setVisibility(View.GONE);

                    changeActionBarText("Add HouseKeeper");
                }
            }
            else if (choice.equals("managerDetailView"))
            {
                getHouseKeeperData();
                txtContactNumber.setEnabled(false);
                txtEmail.setEnabled(false);
                txtFullName.setEnabled(false);
                rlEditAndDelete.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                tvPictureText.setText("Profile Picture");
                ivUserImage.setEnabled(false);
                changeActionBarText("View HouseKeeper");
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        currentDate = simpleDateFormat.format(date);




        txtContactNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int Count, int after)
            {
                previousContactNumber = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int Count)
            {


            }

            @Override
            public void afterTextChanged(Editable s)
            {
                int lengthOfText = s.length();
                if (previousContactNumber.length() < lengthOfText) // True when user add text , False when user removing text
                {
                    if (lengthOfText == 3 || lengthOfText == 7)
                    {
                        String data;
                        data = s.toString() + "-";
                        txtContactNumber.setText(data);
                        txtContactNumber.setSelection(data.length());

                    }
                }
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }


    private  void getHouseKeeperData()
    {


            houseKeeperModel = (HouseKeeperModel) getIntent()
                    .getParcelableExtra("houseKeeperModel");


        if(houseKeeperModel.getProfilePic().equals("") || houseKeeperModel.getProfilePic() == null)
        {
            ivUserImage.setImageResource(R.drawable.icon_user);
        }
        else
        {
            Picasso.with(activity)
                    .setIndicatorsEnabled(true);
            Picasso.with(activity)
                    .load(houseKeeperModel.getProfilePic())
                    .placeholder(R.drawable.loader)

                    .into(ivUserImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

            txtContactNumber.setText(houseKeeperModel.getContactNumber());
            txtPassword.setVisibility(View.GONE);
            txtFullName.setText(houseKeeperModel.getFullName());
            txtEmail.setText(houseKeeperModel.getEmail());



    }
    public void changeActionBarText(String text)
    {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }

    public void uploadUserImage()
    {
        StorageReference imagePath = FirebaseStorage.getInstance().getReference().child("images").child(imageUri.getLastPathSegment());

        imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                try
                {
                    Uri downloadImageUrl = taskSnapshot.getDownloadUrl();

                    addHouseKeeper(downloadImageUrl+"");


                }
                catch (Exception e)
                {
                    spinKit.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addHouseKeeper(String downloadImageUrl)
    {
        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        String houseKeeperId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference database = firebaseFactory.getDatabaseRef().child("users").child(houseKeeperId);
        database.child("email").setValue(txtEmail.getText().toString());
        database.child("password").setValue(txtPassword.getText().toString());
        database.child("userId").setValue(houseKeeperId);
        database.child("role").setValue("housekeeper");

        DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("housekeeper").child(houseKeeperId);
        database2.child("email").setValue(txtEmail.getText().toString());
        database2.child("fullName").setValue(txtFullName.getText().toString());
        database2.child("password").setValue(txtPassword.getText().toString());
        database2.child("userId").setValue(houseKeeperId);
        database2.child("isBusy").setValue("0");
        database2.child("taskId").setValue("0");
        database2.child("locationId").setValue(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_ID, ""));
        database2.child("locationName").setValue(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SELECTED_LOCATION_NAME, ""));
        database2.child("profilePic").setValue(downloadImageUrl + "");
        database2.child("contactNumber").setValue(txtContactNumber.getText().toString());
        database2.child("createdByAdmin").setValue(AppController.getInstance().getPrefManger().getUserProfile().getUserId());
        database2.child("date").setValue(currentDate);
        database2.child("rating").setValue("0.0");
        spinKit.setVisibility(View.GONE);

        btnSave.setText("Save");
        btnSave.setClickable(true);
        Toast.makeText(activity, "Successfully Created HouseKeeper", Toast.LENGTH_LONG).show();
        finish();
    }


    @OnClick({R.id.ivUserImage, R.id.btnSave, R.id.btnEdit, R.id.btnDelete})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivUserImage:
                selectUserImage(IMAGE_SELECT_PERMISSION);
                break;
            case R.id.btnSave:
                if (checkValidation())
                {
                    btnSave.setText("Please Wait...");
                    btnSave.setClickable(false);
                    spinKit.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                if(imageUri == null)
                                {
                                    addHouseKeeper("");
                                }
                                else
                                {
                                    uploadUserImage();
                                }

                            } else
                            {
                                btnSave.setText("SAVE");
                                btnSave.setClickable(true);
                                Toast.makeText(activity, "This Email Is Already Assign To User", Toast.LENGTH_LONG).show();
                                spinKit.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
            case R.id.btnEdit:
                if (AppConstants.isOnline(activity))
                {
                    btnEdit.setClickable(false);
                    if (imageUri != null)
                    {

                        spinKit.setVisibility(View.VISIBLE);
                        StorageReference imagePath = FirebaseStorage.getInstance().getReference().child("images").child(imageUri.getLastPathSegment());

                        imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadImageUrl = taskSnapshot.getDownloadUrl();
                                editHouseKeeper(downloadImageUrl + "");

                            }
                        });
                    }
                    else
                    {
                        spinKit.setVisibility(View.VISIBLE);
                        editHouseKeeper("");
                    }
                }
                break;
            case R.id.btnDelete:
                String houseKeeperId = houseKeeperModel.getUserId();


                FirebaseFactory firebaseFactory2 = FirebaseFactory.getFirebaseFactory();

                DatabaseReference database2 = firebaseFactory2.getDatabaseRef().child("housekeeper").child(houseKeeperId);
                database2.removeValue();

                DatabaseReference database3 = firebaseFactory2.getDatabaseRef().child("users").child(houseKeeperId);
                database3.removeValue();
                Toast.makeText(activity, "Successfully Deleted HouseKeeper", Toast.LENGTH_LONG).show();
                activity.finish();

                break;
        }
    }

    private void editHouseKeeper(String downloadImageUrl)
    {
        String houseKeeperId = houseKeeperModel.getUserId();



        FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

        DatabaseReference database2 = firebaseFactory.getDatabaseRef().child("housekeeper").child(houseKeeperId);
        database2.child("fullName").setValue(txtFullName.getText().toString());

        if (imageUri != null) {
            database2.child("profilePic").setValue(downloadImageUrl + "");
        }

        database2.child("contactNumber").setValue(txtContactNumber.getText().toString());
        database2.child("date").setValue(currentDate);
        spinKit.setVisibility(View.GONE);
        btnEdit.setClickable(true);
        Toast.makeText(activity, "Successfully updated HouseKeeper", Toast.LENGTH_LONG).show();
        activity.finish();


    }


    private void selectUserImage(int a) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, a);
    }

    private boolean checkValidation() {
        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtFullName.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Enter Correct Full Name", Toast.LENGTH_LONG).show();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {

            Toast.makeText(activity, "Enter Correct Email", Toast.LENGTH_LONG).show();

            return false;
        } else if (txtPassword.getText().toString().trim().length() < 6) {
            new CustomAlertDialog(activity, "Alert", "Enter Password Minimum Of 6 Letters");
            return false;
        }
        else if (txtContactNumber.getText().length() > 0)
        {
            Pattern phonePattern
                    = Pattern.compile("[\\d]{3}[-][\\d]{3}[-][\\d]{4,}");
            if(phonePattern.matcher(txtContactNumber.getText().toString()).matches())
            {
                return  true;
            }
            else
            {
                Toast.makeText(activity, "Please Enter Proper Phone Number", Toast.LENGTH_LONG).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == IMAGE_SELECT_PERMISSION && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(activity);
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



    // --> Start setTypeFace() method.
    private void setTypeFace()
    {
        tvPictureText.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtContactNumber.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtEmail.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtFullName.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtPassword.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnSave.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnEdit.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnDelete.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
    }// <--End setTypeFace() method.
}
