package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homeActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    ImageView logoutBtn;
    ImageView addMoney;
    ImageView payMoney;
    TextView balance;
    static int val;
    TextView userName;
    String sUserName;

    DatabaseReference reference,databaseReferenceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitializeFields();
        updateUserName();
        updateBalance();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             mAuth.signOut();
                                             gotoStartActivity();
                                             finish();
                                         }
                                     }
        );
        payMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddContact();
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDepositMoney();
            }
        });


    }

    private void updateUserName() {
        if (currentUser == null) {
            gotoStartActivity();
        } else {
            databaseReferenceName = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            databaseReferenceName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sUserName = dataSnapshot.child("name").getValue().toString();
                    userName.setText(sUserName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void gotoDepositMoney() {
        Intent gotopDepositMoney=new Intent(homeActivity.this,DepositmoneyActivity.class);
        startActivity(gotopDepositMoney);
    }

    private void updateBalance() {


        if(currentUser==null){
            gotoStartActivity();
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference().child("balances").child(currentUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String newBalance = dataSnapshot.child("balance").getValue().toString();
                        balance.setText("Balance= $ " + newBalance);
                        val=Integer.parseInt(newBalance);
                    } else
                        balance.setText("Balance =$0");
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void gotoAddContact() {
        Intent gotopAddContact=new Intent(homeActivity.this,Addcontacts.class);
        startActivity(gotopAddContact);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){
            gotoStartActivity();
        }
    }

    private void gotoStartActivity() {
        Intent gotoStartActivity= new Intent(homeActivity.this,StartActivity.class);
        startActivity(gotoStartActivity);
    }

    private void InitializeFields() {
        balance=findViewById(R.id.balanceTextBox);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userName=findViewById(R.id.userNameHomeTextView);
        logoutBtn=findViewById(R.id.logoutbtn);
        addMoney=findViewById(R.id.imageAddMoney);
        payMoney=findViewById(R.id.imagePayMoney);
    }
}

