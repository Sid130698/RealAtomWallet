package com.example.atomwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddmoneyActivity extends AppCompatActivity {
    ImageView addContactBtn,selectContactBtn;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        InitializeFields();
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContacts();
            }
        });
    }

    private void addContacts() {
        goToAddContacts();
    }

    private void goToAddContacts() {
        Intent gotToAddContacts= new Intent(this,Addcontacts.class);
        startActivity(gotToAddContacts);
    }

    private void InitializeFields() {
        addContactBtn=findViewById(R.id.addContactImageView);
        selectContactBtn=findViewById(R.id.selectContactImageView);
    }
}
