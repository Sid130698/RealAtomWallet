package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {
    boolean isUserActive=false;
    EditText userId;
    EditText password;
    Button submitButton;
    TextView createNewAccount;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;





    private void gotoStartActivity() {
        Intent gotoStartActivity=new Intent(MainActivity.this,StartActivity.class );
        startActivity(gotoStartActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeFields();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserTologIN();
            }
        });
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoreg();
            }
        });


    }

    private void gotoreg() {
        Intent gotoReg= new Intent(MainActivity.this,RegistrationActivity.class);
        startActivity(gotoReg);
    }

    private void allowUserTologIN() {
        String uEmail,uPassword;
        uEmail=userId.getText().toString();
        uPassword=password.getText().toString();
        if(TextUtils.isEmpty(uEmail)||TextUtils.isEmpty(uPassword))
        {
            Toast.makeText(this,"Email-id or password field is empty",Toast.LENGTH_LONG).show();

        }
        else{
            loadingBar.setTitle("Loging In");
            loadingBar.setMessage("Please wait.. Logging in......");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                        gotoHomePage();
                        loadingBar.dismiss();
                    }
                    else{
                             String message=task.getException().toString();
                        Toast.makeText(MainActivity.this,"Error is :"+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }) ;


        }
    }

    private void gotoHomePage() {
        Intent gotoHomePage= new Intent(MainActivity.this,homeActivity.class);
        gotoHomePage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoHomePage);
        finish();
    }

    private void InitializeFields() {
        userId=findViewById(R.id.userIdEditText);
        password=findViewById(R.id.passwordEditText);
        submitButton=findViewById(R.id.submitButton);
        createNewAccount=findViewById(R.id.create_new_account);
        loadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

    }
}
