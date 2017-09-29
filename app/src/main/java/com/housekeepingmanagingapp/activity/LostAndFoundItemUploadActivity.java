package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.housekeepingmanagingapp.adapters.LostAndFoundImagesCurrentAdapter;
import com.housekeepingmanagingapp.adapters.LostAndFoundImagesPreviousAdapter;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LostAndFoundItemUploadActivity extends AppCompatActivity {


    @BindView(R.id.activity_lost_and_found_item_upload)
    RelativeLayout activityLostAndFoundItemUpload;
    @BindView(R.id.tvPreviousImages)
    TextView tvPreviousImages;
    @BindView(R.id.rvPreviousImages)
    RecyclerView rvPreviousImages;
    @BindView(R.id.tvCurrentImages)
    TextView tvCurrentImages;
    @BindView(R.id.rvCurrentImages)
    RecyclerView rvCurrentImages;
    @BindView(R.id.btnSelectImages)
    Button btnSelectImages;
    @BindView(R.id.tvDescriptionText)
    TextView tvDescriptionText;
    @BindView(R.id.txtDescription)
    EditText txtDescription;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    private int IMAGE_SELECT_PERMISSION = 100;

    Activity activity;
    DatabaseReference database;
    Uri imageUri;

    private ArrayList<String> lostAndFoundPreviousImages = new ArrayList<>();
    private ArrayList<Uri> lostAndFoundCurrentImages = new ArrayList<>();
    private int i;
    private  int duration=20000;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_item_upload);
        ButterKnife.bind(this);

        activity = this;

        changeActionBarText("Lost And Found Items");

        if(AppController.getInstance().getPrefManger().getUserProfile().getRole().equals("manager"))
        {
            btnSelectImages.setVisibility(View.GONE);
            txtDescription.setEnabled(false);
            tvDescriptionText.setText("View Description");
        }
        else if(AppController.getInstance().getPrefManger().getUserProfile().getRole().equals("housekeeper"))
        {
            FirebaseFactory firebaseFactory = FirebaseFactory.getFirebaseFactory();

            database = firebaseFactory.getDatabaseRef().child("tasks").child(AppConstants.taskModel.getTaskId());
        }

        btnUpload.setVisibility(View.GONE);
        rvCurrentImages.setVisibility(View.GONE);
        tvCurrentImages.setVisibility(View.GONE);

        if(AppConstants.taskModel.getLostAndFoundItemsDescription().toString().length()> 0)
        {
            txtDescription.setText(AppConstants.taskModel.getLostAndFoundItemsDescription().toString());
        }
        if (AppConstants.taskModel.getLostAndFoundItemsImages().size() == 0)
        {
            rvPreviousImages.setVisibility(View.GONE);
            tvPreviousImages.setVisibility(View.GONE);

        }
        else if (AppConstants.taskModel.getLostAndFoundItemsImages().size() > 0)
        {
            LostAndFoundImagesPreviousAdapter adapter = new LostAndFoundImagesPreviousAdapter(activity, AppConstants.taskModel.getLostAndFoundItemsImages());
            rvPreviousImages.setAdapter(adapter);
            tvPreviousImages.setText("Previously Uploaded Images: "+AppConstants.taskModel.getLostAndFoundItemsImages().size());
        }
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(activity, 2);
        rvPreviousImages.setLayoutManager(layoutManager);
        rvPreviousImages.setFocusable(false);

        RecyclerView.LayoutManager layoutManager2;
        layoutManager2 = new GridLayoutManager(activity, 2);
        rvCurrentImages.setLayoutManager(layoutManager2);
        rvCurrentImages.setFocusable(false);
    }

    @OnClick({R.id.btnSelectImages, R.id.btnUpload})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSelectImages:
                if(lostAndFoundCurrentImages.size() == 2)
                {
                    Toast.makeText(activity, "You Can Upload Only 2 Images At A Time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    imageUri = null;
                    selectUserImage(IMAGE_SELECT_PERMISSION);
                }
                break;
            case R.id.btnUpload:

                spinKit.setVisibility(View.VISIBLE);
                for (i = 0; i < lostAndFoundCurrentImages.size(); i++)
                {
                    imageUri =  lostAndFoundCurrentImages.get(i);
                    StorageReference imagePath = FirebaseStorage.getInstance().getReference().child("images").child(imageUri.getLastPathSegment());

                    imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            try
                            {
                                Uri downloadImageUrl = taskSnapshot.getDownloadUrl();

                                AppConstants.taskModel.getLostAndFoundItemsImages().add(downloadImageUrl + "");
                                AppConstants.taskModel.setLostAndFoundItemsDescription(txtDescription.getText().toString());

                                database.setValue(AppConstants.taskModel);


                            } catch (Exception e)
                            {

                            }
                        }
                    });


                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(activity, "Images Uploaded", Toast.LENGTH_SHORT).show();
                            spinKit.setVisibility(View.GONE);
                            activity.finish();
                        }
                    },duration);



                }

                break;
        }
    }

    private void selectUserImage(int a) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, a);
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

                lostAndFoundCurrentImages.add(imageUri);

                Log.e("test", "lostAndFoundCurrentImages " + lostAndFoundCurrentImages.size());

                LostAndFoundImagesCurrentAdapter adapter = new LostAndFoundImagesCurrentAdapter(activity, lostAndFoundCurrentImages);
                rvCurrentImages.setAdapter(adapter);

                if (rvCurrentImages.getVisibility() == View.GONE)
                {
                    rvCurrentImages.setVisibility(View.VISIBLE);
                    tvCurrentImages.setVisibility(View.VISIBLE);
                    btnSelectImages.setText("SELECT NEXT IMAGE");
                    btnUpload.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }

    public void changeActionBarText(String text) {
        ActionBar a = getSupportActionBar();
        a.setCustomView(R.layout.layout_actionbar);
        a.setDisplayShowCustomEnabled(true);
        TextView tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvActionBarTitle.setText(text);
    }
}
