package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity implements ItemAdapter.ClickHandler {

    RecyclerView rvItemList;
    TextView tvCount,tvContinue,tvHeading;
    ItemAdapter myAdapter;
    int count=0;
    ArrayList<Item> items;
    public static ArrayList<Item> selectedItems;
    DatabaseReference shopRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        initialize();
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBilling();
            }
        });
    }

    private void startBilling()
    {
        if(count==0)
        {
            Toast.makeText(this,"No item selected",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(PurchaseActivity.this,BillActivity.class);
        intent.putExtra("shop",getIntent().getStringExtra("shop"));
        startActivityForResult(intent,1);
    }
    private void initialize() {
        rvItemList=findViewById(R.id.rvItemList);
        tvCount=findViewById(R.id.tvCount);
        tvContinue=findViewById(R.id.tvContinue);
        tvHeading=findViewById(R.id.tvHeading);
        tvHeading.setText(getIntent().getStringExtra("shop"));
        rvItemList.setHasFixedSize(true);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));
        items=new ArrayList<>();
        selectedItems=new ArrayList<>();
        shopRef= FirebaseDatabase.getInstance().getReference("stocks").child(getIntent().getStringExtra("shop"));
        shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item item;
                for(DataSnapshot snap: dataSnapshot.getChildren())
                {
                    item=snap.getValue(Item.class);
                    if(item.getQuantity()!=0)
                        items.add(item);
                }
                myAdapter=new ItemAdapter(PurchaseActivity.this,items,ItemAdapter.MODE_SELECT);
                rvItemList.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClicked(int index) {
        ++count;
        tvCount.setText(count+"");
        selectedItems.add(new Item(items.get(index)));
        items.remove(index);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeItemCount(boolean isRight, int index) {
//Empty Function Required
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
            Toast.makeText(PurchaseActivity.this,"Transaction Failed",Toast.LENGTH_SHORT).show();
        finish();
    }
}
