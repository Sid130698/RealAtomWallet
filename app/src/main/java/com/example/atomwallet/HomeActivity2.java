package com.example.atomwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity2 extends AppCompatActivity {

    ImageView imageOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        initialize();
        imageOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity2.this,OrdersActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivityForResult(intent,1);

            }
        });
    }

    private void initialize() {
        imageOrders=findViewById(R.id.imageOrders);
    }
}
