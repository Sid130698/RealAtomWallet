package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
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

public class BillActivity extends AppCompatActivity implements ItemAdapter.ClickHandler {
    TextView tvBalance,tvFinalAmount,tvContinue;
    RecyclerView rvSelectedItems;
    int[] count;
    ItemAdapter myAdapter;
    double finalAmount=0,balance;
    DatabaseReference balanceRef,orderRef;
    String orderNumber,individualItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initialize();
        tvFinalAmount.setText("Total Amount="+finalAmount);
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPurchase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
            Toast.makeText(this,"Transaction Failed",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishPurchase() {
        if(finalAmount>balance)
        {
            Toast.makeText(this,"Balance low",Toast.LENGTH_SHORT).show();
            return;
        }
        orderNumber=orderRef.push().getKey();
        orderRef=orderRef.child(orderNumber);
        for(int i=0;i<PurchaseActivity.selectedItems.size();++i)
        {
            Item item=PurchaseActivity.selectedItems.get(i);
            individualItem=orderRef.push().getKey();
            orderRef.child(individualItem).setValue(new OrderDetails(item.getId(),count[i]));
        }
        Intent intent= new Intent(BillActivity.this,PayActivity.class);
        intent.putExtra("ID",codify(orderNumber));
        intent.putExtra("shop",getIntent().getStringExtra("shop"));
        intent.putExtra("amount",finalAmount);
        startActivityForResult(intent,1);



    }

    private void initialize() {
        tvBalance=findViewById(R.id.tvBalance);
        tvFinalAmount=findViewById(R.id.tvFinalAmount);
        tvContinue=findViewById(R.id.tvContinue);
        rvSelectedItems=findViewById(R.id.rvSelectedItems);
        rvSelectedItems.setHasFixedSize(true);
        rvSelectedItems.setLayoutManager(new LinearLayoutManager(this));
        count=new int[PurchaseActivity.selectedItems.size()];
        for(int i=0;i<count.length;++i)
        {
            count[i]=1;
            finalAmount+=PurchaseActivity.selectedItems.get(i).getPrice();
        }
        myAdapter=new ItemAdapter(this,PurchaseActivity.selectedItems,ItemAdapter.MODE_BILLING);
        rvSelectedItems.setAdapter(myAdapter);
        balanceRef = FirebaseDatabase.getInstance().getReference("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
       orderRef=FirebaseDatabase.getInstance().getReference("orders").child(getIntent().getStringExtra("shop"));
        balanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String newBalance = dataSnapshot.child("balance").getValue().toString();
                    balance=Double.parseDouble(newBalance);
                    tvBalance.setText("Current balance="+balance);
                    if(balance<finalAmount) tvFinalAmount.setTextColor(Color.RED);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BillActivity.this,"Connectivity error",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    @Override
    public void onItemClicked(int index) {
//Empty Body Required
    }

    @Override
    public void changeItemCount(boolean isRight, int index) {
        Item item=PurchaseActivity.selectedItems.get(index);
        if(isRight)
        {
            if((count[index]+1)>item.getQuantity())
            {
                Toast.makeText(BillActivity.this,"Going out of stock",Toast.LENGTH_SHORT).show();
                return;
            }
            ++count[index];
            finalAmount+=item.getPrice();
            Toast.makeText(BillActivity.this,count[index]+"",Toast.LENGTH_SHORT).show();
            if(finalAmount>balance) tvFinalAmount.setTextColor(Color.RED);

        }
        else{
            if((count[index]-1)<=0) return;
            --count[index];
            finalAmount-=item.getPrice();
            Toast.makeText(BillActivity.this,count[index]+"",Toast.LENGTH_SHORT).show();
            if(finalAmount<=balance) tvFinalAmount.setTextColor(Color.BLACK);
        }
        tvFinalAmount.setText("Total Amount="+finalAmount);
    }

    public static String codify(String s)
    {
        if(s==null)
            return "";
        String result="";
        result+=(s.length()%10);
        result+=(s.charAt(0)%10);
        result+=(s.charAt(s.length()-1)%10);
        result+=(s.charAt(s.length()/2)%10);
        int total=0;
        for(int i=0;i<s.length();++i)
            total+=s.charAt(i);
        result+=(total%10);
        return result;
    }
}
