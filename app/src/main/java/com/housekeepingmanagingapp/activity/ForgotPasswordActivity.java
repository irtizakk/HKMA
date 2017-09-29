package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {
    Session session = null;
    Activity activity;
    String email, password;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.btnGetPassword)
    Button btnGetPassword;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        changeActionBarText("Forgot Password");

        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (id == android.R.id.home)
        {
            onBackPressed();
            return  true;
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

    private void getPassword() {

        email = txtEmail.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(txtEmail.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        password = data.child("password").getValue().toString();

                    }

                    sendEmail();
                }
                else
                {
                    spinKit.setVisibility(View.INVISIBLE);
                    btnGetPassword.setText("Get Password");
                    Toast.makeText(activity, "This Email is not exists", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                btnGetPassword.setText("Get Password");
                spinKit.setVisibility(View.INVISIBLE);
            }

        });
    }

    public void sendEmail()
    {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("createdeveloper2017@gmail.com", "?mystry123?");

            }
        });

        r a = new r();
        a.execute();
    }

    @OnClick(R.id.btnGetPassword)
    public void onClick() {


        if (AppConstants.isOnline(activity)) {
            spinKit.setVisibility(View.VISIBLE);
            getPassword();
        } else {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    class r extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("createdeveloper2017@gmail.com"));
                String s = email + "";
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s));
                msg.setSubject("HouseKeeping App");
                msg.setContent("Dear User Your Password Is "+password, "text/html; charset=utf-8");
                Transport.send(msg);

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid)
        {
            Toast.makeText(activity, "Password Has Been Send To Your Email Address", Toast.LENGTH_LONG).show();
            spinKit.setVisibility(View.GONE);
            btnGetPassword.setText("Get Password");
        }
    }
}
