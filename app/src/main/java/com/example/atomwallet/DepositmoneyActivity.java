package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DepositmoneyActivity extends AppCompatActivity {
EditText amountDeposit;
ImageView depositImage;
FirebaseAuth mAuth;
FirebaseUser currentUser;
DatabaseReference rootRef,reference,checkRef;
int oldValue,newValue;
TextView depositMoneyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositmoney);
        Initializer();
        depositImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositMoney();

            }
        });
        depositMoneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositMoney();
            }
        });
    }

    private void DepositMoney() {
        final String amountDepositByUser,currentUserId;
        currentUserId=currentUser.getUid();
        amountDepositByUser=amountDeposit.getText().toString();
        newValue=Integer.parseInt(amountDepositByUser);

        if(TextUtils.isEmpty(amountDepositByUser)){
            Toast.makeText(this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
        }
        else {
            if (currentUser == null) {
                rootRef.child("balances").child(currentUserId).setValue("");
                HashMap<String, String> balanceHash = new HashMap<>();
                balanceHash.put("balance", amountDepositByUser);
                balanceHash.put("uid", currentUserId);
                rootRef.child("balances").child(currentUserId).setValue(balanceHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DepositmoneyActivity.this, "Amount Added to Wallet " + amountDepositByUser, Toast.LENGTH_LONG).show();
                            gotoHomeScreen();
                        }

                    }
                });
            } else {
                        final String newBal;
                        newValue+=homeActivity.val;
                        newBal=Integer.toString(newValue);
                        reference=FirebaseDatabase.getInstance().getReference().child("balances").child(currentUser.getUid());
                        reference.child("balance").setValue(newBal).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    DatabaseReference trans=rootRef.child("transactions").child(currentUserId);
                                    String key=trans.push().getKey();
                                    trans.child(key).setValue(new Transaction("",currentUserId,amountDepositByUser));
                                    Toast.makeText(DepositmoneyActivity.this, "Amount updated to Wallet " + newBal, Toast.LENGTH_LONG).show();
                                    gotoHomeScreen();
                                }
                            }
                        });


            }
        }}

    private void gotoHomeScreen() {
        Intent gotoHomeScreen=new Intent(DepositmoneyActivity.this,homeActivity.class);
        startActivity(gotoHomeScreen);
    }

    private void Initializer() {
        amountDeposit=findViewById(R.id.amountForDeposition);
        depositImage=findViewById(R.id.depositMoneyImageView);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        rootRef= FirebaseDatabase.getInstance().getReference();
        depositMoneyText=findViewById(R.id.depositMoneyTextView);

    }
}
