package com.example.atomwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
    ImageView loginButton,registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        InitializeFields();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
    }

    private void gotoRegisterActivity() {
        Intent gotoRegisterActivity=new Intent(StartActivity.this,RegistrationActivity.class );
        startActivity(gotoRegisterActivity);

    }

    private void gotoLoginActivity() {
        Intent gotoLoginActivity=new Intent(StartActivity.this,MainActivity.class );
        startActivity(gotoLoginActivity);
    }

    private void InitializeFields() {
        loginButton=findViewById(R.id.loginBtn);
        registerButton=findViewById(R.id.registerBtn);

    }
}
