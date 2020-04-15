package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActvity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    EditText message;
    ImageButton sendButton;
    String currentuserId,recieversuserId;
    DatabaseReference recieverNameRef,rootref;
    private final List<messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private chatAdaptor varChatAdaptor;
    private RecyclerView userMessageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_actvity);
        Initfields();
        recieversuserId=getIntent().getExtras().get("visitorUserId").toString();
        recieverNameRef=FirebaseDatabase.getInstance().getReference().child("users").child(recieversuserId);
        recieverNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                Toast.makeText(ChatActvity.this, "Inside chat"+name+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rootref.child("Messages").child(currentuserId).child(recieversuserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages varMessages=dataSnapshot.getValue(messages.class);
                messagesList.add(varMessages);
                varChatAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserMessage() {
        final String userMessage;
        userMessage=message.getText().toString();
        if(TextUtils.isEmpty(userMessage)){
            Toast.makeText(this, "Empty message cannot be send", Toast.LENGTH_SHORT).show();
        }
        else{
            String messageSenderRef,messageRecieverRef;
            messageSenderRef="Messages/"+currentuserId+"/"+recieversuserId;
            messageRecieverRef="Messages/"+recieversuserId+"/"+currentuserId;
            DatabaseReference userMessageKeyRef= rootref.child("Messages").child(currentuserId).child(recieversuserId).push();
            String messagePushId=userMessageKeyRef.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message",userMessage);
            messageTextBody.put("from",currentuserId);
            messageTextBody.put("to",recieversuserId);
            messageTextBody.put("messageId",messagePushId);
            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushId,messageTextBody);
            messageBodyDetails.put(messageRecieverRef+"/"+messagePushId,messageTextBody);
            rootref.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActvity.this, "message Sent Succesfully", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(ChatActvity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    message.setText("");
                }
            });


        }
    }

    private void Initfields() {
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        message=findViewById(R.id.chatActivityEditText);
        sendButton=findViewById(R.id.sendButtonChatActivity);
        varChatAdaptor=new chatAdaptor(messagesList);
        userMessageList= findViewById(R.id.chatActivityRecyclerView);
        linearLayoutManager=new LinearLayoutManager(this);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(varChatAdaptor);


        if(currentuser==null){
            gotoStartActivity();
        }
        else{
            currentuserId=currentuser.getUid();
            rootref=FirebaseDatabase.getInstance().getReference();
        }
    }

    private void gotoStartActivity() {
        Intent gotoStartActivity = new Intent(ChatActvity.this,StartActivity.class);
        startActivity(gotoStartActivity);
    }
}
