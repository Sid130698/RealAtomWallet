package com.example.atomwallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    ArrayList<Transaction> list;
    String currUserID;
    DatabaseReference userRef;


    public TransactionAdapter(ArrayList<Transaction> list, String currUserID) {
        this.list=list;
        this.currUserID= currUserID;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivTask;
        TextView tvName,tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTask=itemView.findViewById(R.id.ivTask);
            tvName=itemView.findViewById(R.id.tvName);
            tvAmount=itemView.findViewById(R.id.tvAmount);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false);
        return new TransactionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Transaction transaction =list.get(position);
        holder.tvAmount.setText(transaction.getAmount());
        if(transaction.isPurchase())
        {
            holder.ivTask.setImageResource(R.drawable.cart);
            userRef=FirebaseDatabase.getInstance().getReference("shops");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Shop shop;
                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        shop=snap.getValue(Shop.class);
                        if(shop.getId().equals(transaction.getReciever()))
                            holder.tvName.setText(shop.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
        userRef= FirebaseDatabase.getInstance().getReference("users");
        String name;
        if(transaction.getSender().isEmpty())
        {
            holder.ivTask.setImageResource(R.drawable.add);
            holder.tvName.setText("Added Money");
        }
        else if(transaction.getSender().equals(currUserID)) {

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user;
                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        user=snap.getValue(User.class);
                        if(user.getUserId().equals(transaction.getReciever()))
                        {
                            holder.tvName.setText(user.getName());
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.ivTask.setImageResource(R.drawable.send);

        }
        else {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user;
                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        user=snap.getValue(User.class);
                        if(user.getUserId().equals(transaction.getSender()))
                        {
                            holder.tvName.setText(user.getName());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.ivTask.setImageResource(R.drawable.recieve);

        }}

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
