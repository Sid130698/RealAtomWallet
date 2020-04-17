package com.example.atomwallet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements ShopAdapter.ClickHandler {

    ArrayList<String> shops;
    RecyclerView rvShops;
    ShopAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initialize();
    }

    private void initialize() {
        shops=new ArrayList<>();
        shops.add("Sri Kateel Bakery");
        shops.add("Royal Cafe");
        shops.add("Maggi Station");
        shops.add("La Casa");
        shops.add("NMIT Bakery");
        rvShops=findViewById(R.id.shopList);
        rvShops.setHasFixedSize(true);
        rvShops.setLayoutManager(new LinearLayoutManager(this));
        myAdapter=new ShopAdapter(this,shops);
        rvShops.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(String shop) {
        Intent intent= new Intent(ShopActivity.this,PurchaseActivity.class);
        intent.putExtra("shop",shop);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        finish();
    }
}
