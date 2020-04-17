package com.example.atomwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    ArrayList<String> shops;
    ClickHandler activity;
    public interface ClickHandler
    {
        void onItemClick(String shop);
    }

    public ShopAdapter(Context context,ArrayList<String> list)
    {
        activity=(ClickHandler) context;
        shops=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card,parent,false);
        return new ShopAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSign.setText(shops.get(position).toUpperCase().charAt(0)+"");
        holder.tvName.setText(shops.get(position));
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSign,tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSign=itemView.findViewById(R.id.tvSign);
            tvName=itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClick(tvName.getText().toString());
                }
            });
        }
    }
}
