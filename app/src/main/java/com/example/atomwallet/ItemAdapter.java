package com.example.atomwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<Item> itemList;
    ClickHandler activity;
    public static final int MODE_SELECT=1;
    public static final int MODE_BILLING=2;
    int currMode;

    public ItemAdapter(Context context,ArrayList<Item> list,int mode)
    {
        itemList=list;
        activity=(ClickHandler) context;
        currMode=mode;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        return new ItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currItem=itemList.get(position);
        holder.itemView.setTag(currItem);
        holder.tvName.setText(currItem.getName());
        holder.tvPrice.setText(currItem.getPrice()+"");
        if(currMode==MODE_SELECT)
        {
            holder.imagePlus.setVisibility(View.GONE);
            holder.imageMinus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface ClickHandler
    {
        void onItemClicked(int index);
        void changeItemCount(boolean isRight,int index);

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName,tvPrice;
        ImageView imagePlus,imageMinus;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            imageMinus=itemView.findViewById(R.id.imageMinus);
            imagePlus=itemView.findViewById(R.id.imagePlus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(itemList.indexOf(itemView.getTag()));
                }
            });
            imageMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.changeItemCount(false,itemList.indexOf(itemView.getTag()));
                }
            });
            imagePlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.changeItemCount(true,itemList.indexOf(itemView.getTag()));
                }
            });
        }
    }
}
