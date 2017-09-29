package com.housekeepingmanagingapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.housekeepingmanagingapp.R;
import com.housekeepingmanagingapp.adapters.MessageDataAdapter;
import com.housekeepingmanagingapp.customClasses.AppController;
import com.housekeepingmanagingapp.customClasses.PrefManager;
import com.housekeepingmanagingapp.models.ChatModel;
import com.housekeepingmanagingapp.models.MessageModel;
import com.housekeepingmanagingapp.singleton.FirebaseFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityChat extends AppCompatActivity
{

    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.ivNoDataAvailable)
    ImageView ivNoDataAvailable;
    @BindView(R.id.tvNoTextAvailable)
    TextView tvNoTextAvailable;
    @BindView(R.id.ivAttachButton)
    ImageView ivAttachButton;
    @BindView(R.id.txtMessageArea)
    EditText txtMessageArea;
    @BindView(R.id.ivSendButton)
    ImageView ivSendButton;

     private String chatId = "";
    private Activity activity;
    private  String currentDate;
    MessageDataAdapter messageDataAdapter = null;

    LinkedHashMap<String, ChatModel> allChatValues = new LinkedHashMap<>();
    LinkedHashMap<String, MessageModel> allMessages = new LinkedHashMap<>();

    DatabaseReference messageReference,chatUId;
    String receiverId,receiverName,senderId,senderName;
      @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        activity = this;
       // changeActionBarText("Chat");

        getSupportActionBar().hide();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date date = new Date();

        currentDate = simpleDateFormat.format(date);


        receiverId = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_ID,"").toString();
        receiverName = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_NAME,"").toString();
        senderId = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_ID,"").toString();
        senderName = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_NAME,"").toString();

        tvUserName.setText(AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_NAME,"").toString());
        chatUId = FirebaseDatabase.getInstance().getReference().child("chatUId");
        messageReference = FirebaseDatabase.getInstance().getReference().child("messages");


        initialize();

        FirebaseFactory.getFirebaseFactory().getDatabaseRef().child("chatUId").addListenerForSingleValueEvent(new ValueEventListener()
                //  query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.e("test", "called " + "ok");
                if(dataSnapshot.hasChildren())
                {
                    GenericTypeIndicator<LinkedHashMap<String, ChatModel>> genericTypeIndicator = new GenericTypeIndicator<LinkedHashMap<String, ChatModel>>() {
                    };
                    allChatValues.putAll(dataSnapshot.getValue(genericTypeIndicator));


                    for (int count = 0; count < allChatValues.size(); count++)
                    {
                        String hashKey = allChatValues.keySet().toArray()[count].toString();

                        String sId = allChatValues.get(hashKey).getSenderId();
                        String rId = allChatValues.get(hashKey).getRecieverId();


                        if ((sId.equals(senderId) && rId.equals(receiverId)) || (sId.equals(receiverId) && rId.equals(senderId))) {


                            chatId = allChatValues.get(hashKey).getChatUid();

                            Log.e("test", "chatId " + chatId);
                            query();
                            //queryForSingleMessage();
                            break;

                        }
                    }
                }
                else
                {
                    ivNoDataAvailable.setVisibility(View.VISIBLE);
                    tvNoTextAvailable.setVisibility(View.VISIBLE);
                    rvChat.setVisibility(View.GONE);


                }

            }

            @Override

            public void onCancelled(DatabaseError databaseError)
            {

            }
        }); // <-- End



    }


    private void initialize()
    {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvChat.setLayoutManager(layoutManager);

//        rvChat.setHasFixedSize(true);
//        rvChat.setItemViewCacheSize(20);
//        rvChat.setDrawingCacheEnabled(true);

    }

    // --> Start, Reading all messages from database
    public void query()
    {



        Query query = FirebaseDatabase.getInstance().getReference("messages")
                .orderByChild("chatUid").equalTo(chatId);

        // messageReference.addValueEventListener(new ValueEventListener()
        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.hasChildren())
                {
                    ivNoDataAvailable.setVisibility(View.GONE);
                    tvNoTextAvailable.setVisibility(View.GONE);
                    rvChat.setVisibility(View.VISIBLE);
                    GenericTypeIndicator<LinkedHashMap<String, MessageModel>> genericTypeIndicator = new GenericTypeIndicator<LinkedHashMap<String, MessageModel>>() {
                    };
                    allMessages.putAll(dataSnapshot.getValue(genericTypeIndicator));

                    messageDataAdapter = new MessageDataAdapter(activity, allMessages);
                    rvChat.setAdapter(messageDataAdapter);
                }
                else
                {
                    ivNoDataAvailable.setVisibility(View.VISIBLE);
                    tvNoTextAvailable.setVisibility(View.VISIBLE);
                    rvChat.setVisibility(View.GONE);
                }


          }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


    }// <-- End, Reading all messages from database

    @OnClick({R.id.ivAttachButton, R.id.ivSendButton})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivAttachButton:

                break;
            case R.id.ivSendButton:

                if(txtMessageArea.getText().toString().length() != 0)
                {
                    if (!chatId.equals(""))
                    {


                        MessageModel messageModel = new MessageModel();
                        String messageKey = messageReference.push().getKey();
                        messageModel.setChatUid(chatId);
                        messageModel.setDate(currentDate);
                        messageModel.setRecieverId(receiverId);
                        messageModel.setRecieverName(receiverName);
                        messageModel.setSenderId(senderId);
                        messageModel.setRead(false);
                        messageModel.setSenderName(senderName);
                        messageModel.setText(txtMessageArea.getText().toString());
                        messageReference.child(messageKey).setValue(messageModel);

                        chatUId.child(chatId).child("text").setValue(txtMessageArea.getText().toString());
                        String receiverImage = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_IMAGE, "").toString();
                        String senderImage = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_IMAGE, "").toString();

                        chatUId.child(chatId).child("recieverImage").setValue(receiverImage);
                        chatUId.child(chatId).child("senderImage").setValue(senderImage);
                        chatUId.child(chatId).child("senderName").setValue(senderName);
                        chatUId.child(chatId).child("recieverName").setValue(receiverName);
                        chatUId.child(chatId).child("senderId").setValue(senderId);
                        chatUId.child(chatId).child("recieverId").setValue(receiverId);
                        chatUId.child(chatId).child("date").setValue(currentDate);
                        txtMessageArea.setText("");


                    }
                    else if (chatId.equals("")) {


                        String messageText = txtMessageArea.getText().toString().trim();

                        //String key = myRef.push().getKey();


                        ChatModel chatModel = new ChatModel();

                        chatId = chatUId.push().getKey();

                        chatModel.setChatUid(chatId);
                        chatModel.setRecieverId(receiverId);
                        chatModel.setRecieverName(receiverName);
                        chatModel.setSenderId(senderId);
                        chatModel.setSenderName(senderName);
                        chatModel.setText(messageText);
                        String receiverImage = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_RECEIVER_IMAGE, "").toString();
                        String senderImage = AppController.getInstance().getPrefManger().getValue(PrefManager.KEY_SENDER_IMAGE, "").toString();
                        chatModel.setRecieverImage(receiverImage);
                        chatModel.setSenderImage(senderImage);
                        chatModel.setDate(currentDate);
                        chatUId.child(chatId).setValue(chatModel);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                        Date date = new Date();

                        String dateOrTime = simpleDateFormat.format(date);

                        MessageModel messageModel = new MessageModel();
                        String messageKey = messageReference.push().getKey();
                        messageModel.setChatUid(chatId);
                        messageModel.setDate(dateOrTime);
                        messageModel.setRead(false);
                        messageModel.setRecieverId(receiverId);
                        messageModel.setRecieverName(receiverName);
                        messageModel.setSenderId(senderId);
                        messageModel.setSenderName(senderName);
                        messageModel.setText(messageText);
                        messageReference.child(messageKey).setValue(messageModel);

                        txtMessageArea.setText("");
                        query();
                    }
                }
                break;
        }
    }
}
