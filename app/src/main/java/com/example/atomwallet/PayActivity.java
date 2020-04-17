package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayActivity extends AppCompatActivity {

    TextView tvAmount,tvId,tvContinue;
    Transaction transaction;
    DatabaseReference currentUserRef,recieverAccountRef,trans;
    String currentUserId,recieverUserId;
    double amount;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initialize();
        tvId.setText("Order ID:"+id);
        tvAmount.setText("Total Amount:"+amount);
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoney();
                Toast.makeText(PayActivity.this,"Transaction SuccessFul",Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initialize() {
        tvAmount=findViewById(R.id.tvAmount);
        tvId=findViewById(R.id.tvId);
        tvContinue=findViewById(R.id.tvContinue);
        id=getIntent().getStringExtra("ID");
        amount=getIntent().getDoubleExtra("amount",0.0);
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recieverUserId=getIDfromShopName(getIntent().getStringExtra("shop"));
        currentUserRef= FirebaseDatabase.getInstance().getReference().child("balances").child(currentUserId);
        recieverAccountRef= FirebaseDatabase.getInstance().getReference().child("balances").child(recieverUserId);
        trans=FirebaseDatabase.getInstance().getReference().child("transactions");
        transaction=new Transaction(currentUserId,recieverUserId,amount+"",true);
    }

    public String getIDfromShopName(String shopName)
    {
        return "vwDbIxd0nKfFHxwM4YsLyvY6pYB3";

    }

    private void sendMoney() {
        recieverAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String amount=dataSnapshot.child("balance").getValue().toString();
                    double  recieversoldamount=Double.parseDouble(amount);
                    updateRecieverBalace(recieversoldamount,recieverUserId);
                    String key=trans.child(recieverUserId).push().getKey();
                    trans.child(recieverUserId).child(key).setValue(transaction);
                    key=trans.child(currentUserId).push().getKey();
                    trans.child(currentUserId).child(key).setValue(transaction);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void updateRecieverBalace(double recieversoldamount,String rid) {
        updateUserBalance();

            int newAmount = (int) (recieversoldamount + amount);

            String newRecieversAmount = Integer.toString(newAmount);
            recieverAccountRef.child("balance").setValue(newRecieversAmount);

    }
    private void updateUserBalance() {
        int newAmount;

        newAmount= (int) (homeActivity.val-amount);
        String currentUserNewAmount=newAmount+"";
        currentUserRef.child("balance").setValue(currentUserNewAmount);

    }
}
