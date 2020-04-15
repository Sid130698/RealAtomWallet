package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rvHistory;
    TransactionAdapter myAdapter;
    ArrayList<Transaction> list;
    String currUserID;
    DatabaseReference transRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initializer();
        transRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren())
                    list.add(snap.getValue(Transaction.class));
                Toast.makeText(HistoryActivity.this,list.size()+"",Toast.LENGTH_SHORT).show();
                myAdapter=new TransactionAdapter(list,currUserID);
                rvHistory.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this,"Could not load",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private void initializer() {
        rvHistory=findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        list=new ArrayList<Transaction>();
        rvHistory.setLayoutManager( new LinearLayoutManager(this));
        currUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        transRef= FirebaseDatabase.getInstance().getReference("transactions").child(currUserID);
    }
}