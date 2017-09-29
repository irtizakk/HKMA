package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.customClasses.AppConstants;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.FontUtils;
import com.housekeepingmanagingapp.dialogs.CustomAlertDialog;
import com.housekeepingmanagingapp.models.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.tvLogo)
    TextView tvLogo;

    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtPassword)
    EditText txtPassword;


    Activity activity;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;

    // --> Start onCreate() method.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        activity = this;
        getSupportActionBar().hide();

        //Applying font on required views
        setTypeFace();



            if (AppController.getInstance().getPrefManger().getUserProfile() != null)
            {
                if (AppController.getInstance().getPrefManger().getUserProfile().getRole().equals("admin")) {
                    startActivity(new Intent(activity, AdminViewLocationsActivity.class));
                } else if (AppController.getInstance().getPrefManger().getUserProfile().getRole().equals("manager")) {
                    startActivity(new Intent(activity, AfterLoginManagerActivity.class));
                } else if (AppController.getInstance().getPrefManger().getUserProfile().getRole().equals("housekeeper")) {
                    startActivity(new Intent(activity, AfterLoginHouseKeeperActivity.class));
                }
                activity.finish();
            }


        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,ForgotPasswordActivity.class));
            }
        });
    }// <--End onCreate() method.


    // --> Start onClick() method.
    @OnClick(R.id.btnSignIn)
    public void onClick() {
        if (checkValidation()) {
            startLoginUser();
        } else {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }

    }
    // <--End onClick() method.

    // --> Start setTypeFace() method.
    private void setTypeFace() {
        tvLogo.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        btnSignIn.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        tvForgotPassword.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtEmail.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
        txtPassword.setTypeface(FontUtils.getFont(activity, FontUtils.MontserratRegular));
    }// <--End setTypeFace() method.

    private boolean checkValidation() {
        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {

            Toast.makeText(activity, "Enter Correct Email", Toast.LENGTH_LONG).show();

            return false;
        } else if (txtPassword.getText().toString().trim().length() == 0) {

            Toast.makeText(activity, "Fill Password Filed", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public  void closeKeyboard(Context c, IBinder windowToken)
    {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    // --> Start  startLoginUser method()
    private void startLoginUser()
    {
        closeKeyboard(activity, txtEmail.getWindowToken());
        closeKeyboard(activity, txtPassword.getWindowToken());
        btnSignIn.setText("Please Wait");

        spinKit.setVisibility(View.VISIBLE);

        final String userPassword = txtPassword.getText().toString();
        final String userEmail = txtEmail.getText().toString();

        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                UserModel userModel = null;

                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        userModel = new UserModel();
                        userModel.setEmail(data.child("email").getValue().toString());
                        userModel.setPassword(data.child("password").getValue().toString());
                        userModel.setRole(data.child("role").getValue().toString());
                        userModel.setUserId(data.child("userId").getValue().toString());

                    }
                }
                if (userModel != null && userModel.getPassword().equals(userPassword))
                {
                    if (userModel.getRole().toString().equals("admin"))
                    {
                        startActivity(new Intent(activity, AdminViewLocationsActivity.class));
                    }
                    else if (userModel.getRole().equals("manager"))
                    {
                        startActivity(new Intent(activity, AfterLoginManagerActivity.class));
                    } else if (userModel.getRole().equals("housekeeper"))
                    {
                        startActivity(new Intent(activity, AfterLoginHouseKeeperActivity.class));
                    }
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    btnSignIn.setText("Login");
                    activity.finish();
                    spinKit.setVisibility(View.INVISIBLE);
                    AppController.getInstance().getPrefManger().setUserProfile(userModel);
                }
                else
                {
                    btnSignIn.setText("Login");
                    String error;
                    spinKit.setVisibility(View.GONE);

                    error = "Email Address Or Password Is Invalid";

                    new CustomAlertDialog(activity, "Alert", error);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                btnSignIn.setText("Login");
                spinKit.setVisibility(View.INVISIBLE);
            }

        });


    }
    // <-- End  startLoginUser method()

}
