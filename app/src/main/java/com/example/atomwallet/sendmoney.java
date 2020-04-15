package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import  java.io.*;

public class sendmoney extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();

    }

    EditText amountEntered;
    TextView sendMoneyText;
    ImageView sendMoneyImg;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;
    int amount=0;
    String recieversName;
    int count=0;
    double phoneNumber;
    DatabaseReference currentUserRef,recieverAccountRef,recieversNameRef,trans;
    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney);
        InitializeFields();
        sendMoneyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMoney();
            }
        });
        sendMoneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMoney();
            }
        });
    }

    private void sendMoney() {

        String sAmountEntered=amountEntered.getText().toString();

        amount=Integer.parseInt(sAmountEntered);
        final String recieverUserId=getIntent().getExtras().get("visitorUserId").toString();
        transaction=new Transaction(currentUserId,recieverUserId,sAmountEntered);
        recieverAccountRef=FirebaseDatabase.getInstance().getReference().child("balances").child(recieverUserId);
        recieverAccountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                String amount=dataSnapshot.child("balance").getValue().toString();
               int  recieversoldamount=Integer.parseInt(amount);
                    updateRecieverBalace(recieversoldamount,recieverUserId);
                    String key=trans.child(recieverUserId).push().getKey();
                    trans.child(recieverUserId).child(key).setValue(transaction);
                    key=trans.child(currentUserId).push().getKey();
                    trans.child(currentUserId).child(key).setValue(transaction);}
                else{
                    Toast.makeText(sendmoney.this, "Couldn't send weaker conection", Toast.LENGTH_SHORT).show();
                    gotoHomeActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        String tempPhoneNumber=Double.toString(phoneNumber);

        gotoHomeActivity();



    }

    private void gotoHomeActivity() {
        Intent gotoHomeActivity = new Intent(sendmoney.this,homeActivity.class);
        startActivity(gotoHomeActivity);
    }

    private void updatteUserBalance(int uamount) {
        int newAmount=uamount;

        newAmount=homeActivity.val-newAmount;
        String currentUserNewAmount=Integer.toString(newAmount);
        currentUserRef.child("balance").setValue(currentUserNewAmount);

    }

    private void updateRecieverBalace(int recieversoldamount,String rid) {
        if (recieversoldamount == 0) {
            Toast.makeText(sendmoney.this, "Couldn't send weaker conection", Toast.LENGTH_SHORT).show();
            gotoHomeActivity();

        } else {

            int newAmount,uamount ;
            newAmount=0;
            newAmount=amount;

            amount=0;
            uamount=newAmount;
            updatteUserBalance(uamount);

            newAmount = recieversoldamount + newAmount;

            String newRecieversAmount = Integer.toString(newAmount);
            recieverAccountRef.child("balance").setValue(newRecieversAmount);
            displayToast(uamount,rid);

        }
    }

    private void displayToast(final int uamount, String recieversId) {
        recieversNameRef=FirebaseDatabase.getInstance().getReference().child("users").child(recieversId);
        recieversNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                doToast(name,uamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doToast(String name,int uamount) {
        if(count==0){
        Toast.makeText(this, "$"+uamount+"sent to "+name, Toast.LENGTH_SHORT).show();
        count++;}
        else
            count++;
    }

    private void InitializeFields() {
        amountEntered=findViewById(R.id.sendMoneyEditText);
        sendMoneyImg=findViewById(R.id.sendMoneyImageView);
        sendMoneyText=findViewById(R.id.sendMoneyTextView);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        currentUserRef= FirebaseDatabase.getInstance().getReference().child("balances").child(currentUserId);
        trans=FirebaseDatabase.getInstance().getReference().child("transactions");

    }
}
