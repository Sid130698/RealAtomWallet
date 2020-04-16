package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class selectpicActivity extends AppCompatActivity {
    Button selectPic,goback;
    private static final int gallerypic=1;
    private StorageReference userProfileImageRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference rootref;
    ProgressDialog loadingBar;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypic&&resultCode==RESULT_OK&&data!=null){
            Uri imageUri=data.getData();
            if(currentUser==null) {
                gotoStartActivity();
            }

        else {
            loadingBar.setTitle("Uploading profile picture");
            loadingBar.setMessage("Please Wait while your profile picture is updated ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            StorageReference filePath = userProfileImageRef.child(currentUser.getUid()+".jpg");
           filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   final Task<Uri> firebaseUri= taskSnapshot.getStorage().getDownloadUrl();
                   firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           final String downloadUrl = uri.toString();
                           if (currentUser == null) {
                               gotoStartActivity();
                           } else {
                               rootref.child("profilePictures").child(currentUser.getUid()).child("profilePic").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           Toast.makeText(selectpicActivity.this, "Profile Picture Uploaded ", Toast.LENGTH_SHORT).show();
                                           loadingBar.dismiss();

                                       }
                                       else{
                                           String message=task.getException().toString();
                                           Toast.makeText(selectpicActivity.this, message, Toast.LENGTH_SHORT).show();
                                           loadingBar.dismiss();
                                       }
                                   }
                               });

                           }
                       }
                   });

               }
           });
        }
        }
    }

    private void gotoStartActivity() {
        Intent gotoStartActivity= new Intent(selectpicActivity.this,StartActivity.class);
        startActivity(gotoStartActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpic);
        InitFields();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHomeactivity();
            }
        });
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePic();
            }
        });
    }

    private void selectProfilePic() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypic);


    }

    private void gotoHomeactivity() {
        Intent gotoHomeActivity= new Intent(selectpicActivity.this,homeActivity.class);
        startActivity(gotoHomeActivity);
    }

    private void InitFields() {
        selectPic=findViewById(R.id.selectPictureBtn);
        goback=findViewById(R.id.goBackButtton);
        userProfileImageRef= FirebaseStorage.getInstance().getReference().child("ProfileImages");
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        rootref= FirebaseDatabase.getInstance().getReference();
        loadingBar=new ProgressDialog(this);
    }
}
