package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class OrdersActivity extends AppCompatActivity {

    TextView tvOrderID,tvIgnore,tvConfirm;
    RecyclerView rvOrderItemList;
    DatabaseReference stockRef,orderRef;
    OrderedItemAdapter myAdapter;
    ArrayList<OrderDetails> list1,list2;
    Item[] newItem;
    String name,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initialize();

       stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item item;
                for(int i=0;i<list1.size();++i)
                {
                    item=dataSnapshot.child(list1.get(i).getItemID()).getValue(Item.class);
                    list2.add(new OrderDetails(item.getName(),list1.get(i).getQuantity()));
                }
                myAdapter=new OrderedItemAdapter(list2);
                rvOrderItemList.setAdapter(myAdapter);
                Toast.makeText(OrdersActivity.this,list2.size()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        id=BillActivity.codify(id);
        tvOrderID.setText("Order ID :"+id);
        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrdersActivity.this,"Order Cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderConfirmed();
            }
        });

    }

    private void orderConfirmed() {
        stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0;i<list1.size();++i)
                {
                    newItem[i]=dataSnapshot.child(list1.get(i).getItemID()).getValue(Item.class);
                    newItem[i].setQuantity(newItem[i].getQuantity()-list1.get(i).getQuantity());

                }
                for(int i=0;i<list1.size();++i)
                    stockRef.child(list1.get(i).getItemID()).setValue(newItem[i]);
                Toast.makeText(OrdersActivity.this,"Order confirmed",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize() {
        tvOrderID=findViewById(R.id.tvOrderID);
        tvIgnore=findViewById(R.id.tvIgnore);
        tvConfirm=findViewById(R.id.tvConfirm);
        rvOrderItemList=findViewById(R.id.rvOrderedItems);
        rvOrderItemList.setHasFixedSize(true);
        rvOrderItemList.setLayoutManager(new LinearLayoutManager(this));
        name=getIntent().getStringExtra("name");
        stockRef= FirebaseDatabase.getInstance().getReference("stocks").child(name);
        orderRef=FirebaseDatabase.getInstance().getReference("orders").child(name);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    id=snap.getKey();
                    for(DataSnapshot snap2 : snap.getChildren())
                    {
                        list1.add(snap2.getValue(OrderDetails.class));
                    }
                    newItem=new Item[list1.size()];
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
