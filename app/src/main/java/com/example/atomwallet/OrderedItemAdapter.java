package com.example.atomwallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.ViewHolder> {

    ArrayList<OrderDetails> list;
    public OrderedItemAdapter(ArrayList<OrderDetails> list)
    {
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName,tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
        }
    }
    @NonNull
    @Override
    public OrderedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card,parent,false);
        return new OrderedItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getItemID());
        holder.tvQuantity.setText(list.get(position).getQuantity()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
