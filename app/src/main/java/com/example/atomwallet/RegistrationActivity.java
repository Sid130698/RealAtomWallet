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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    EditText regUserID,regPassword,regUserName,regUserPhoneNumber;
    String regUserEmail,regUserPassword,name,phoneNumber;
    private FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    Button submitRegButton;
    TextView AlreadyHaveAnAccount;
    DatabaseReference rootref;
    String currentUserId;


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);
            InitializeFields();
            mAuth = FirebaseAuth.getInstance();
            submitRegButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    createAccount();

                }
            });
            AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gotoLoginpage();
                }
            });

        }

    private void createProfile() {
        currentUserId=mAuth.getCurrentUser().getUid();
        rootref.child("users").child(currentUserId).setValue("");
        HashMap<String,String>  profileMap=new HashMap<>();
        profileMap.put("userId",currentUserId);
        profileMap.put("name",name);
        profileMap.put("phoneNumber",phoneNumber);
        profileMap.put("zPassword",regUserPassword);
        rootref.child("users").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();
                }
                else{
                    String Message=task.getException().toString();
                    Toast.makeText(RegistrationActivity.this, "Error "+Message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        HashMap<String,String> defaultBalanceMap=new HashMap<>();
        defaultBalanceMap.put("balance","100");
        defaultBalanceMap.put("uid",currentUserId);
        rootref.child("balances").child(currentUserId).setValue(defaultBalanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Current Balance is $100", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void gotoLoginpage() {
        Intent gotoLoginPage = new Intent(RegistrationActivity.this,MainActivity.class);
        startActivity(gotoLoginPage);
    }


    private void createAccount() {
        regUserEmail = regUserID.getText().toString();
        name=regUserName.getText().toString();
        phoneNumber=regUserPhoneNumber.getText().toString();
        regUserPassword = regPassword.getText().toString();

        if (TextUtils.isEmpty(regUserEmail) || TextUtils.isEmpty(regUserPassword)||TextUtils.isEmpty(name)||TextUtils.isEmpty(phoneNumber))
            Toast.makeText(this, "Please fill all the details ", Toast.LENGTH_SHORT).show();
        else {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("please wait,while we are creating new account for you");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(regUserEmail, regUserPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                        createProfile();
                        gotoHomePage();
                    } else {

                        String message = task.getException().toString();
                        Toast.makeText(RegistrationActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }


                }
            });
        }

    }

    private void gotoHomePage() {
        Intent gotoHomeActivity=new Intent(RegistrationActivity.this,homeActivity.class );
        gotoHomeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoHomeActivity);
        finish();
    }

    private void InitializeFields () {
        regUserID = findViewById(R.id.RegUserIdEditText);
        regPassword = findViewById(R.id.RegPasswordEditText);
        regUserName=findViewById(R.id.yourNameEditText);
        regUserPhoneNumber=findViewById(R.id.phoneNumberEditText);
        loadingBar = new ProgressDialog(this);
        submitRegButton = findViewById(R.id.RegSubmitButton);
        AlreadyHaveAnAccount=findViewById(R.id.already_have_an_account);
        rootref= FirebaseDatabase.getInstance().getReference();
    }
}
