package com.example.atomwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    ImageView loginButton,registerButton;

    TextToSpeech atomwallet;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        InitializeFields();
        speakAtomspeak();

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

    private void speakAtomspeak() { atomwallet.speak("Hello ! There This is Atom Wallet Please login or register ",TextToSpeech.QUEUE_FLUSH,null);
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
        atomwallet=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    atomwallet.setLanguage(Locale.US);
                }

            }
        });

    }
}
