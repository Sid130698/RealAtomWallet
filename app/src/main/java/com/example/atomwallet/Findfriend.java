package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Findfriend extends AppCompatActivity {
RecyclerView findFriendRecyclerList;
FirebaseAuth mAuth;
FirebaseUser currentuser;
DatabaseReference rootref,profilePicRef;
String currentUserId;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(rootref,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts,addContactsView> adapter=new FirebaseRecyclerAdapter<Contacts, addContactsView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final addContactsView holder, final int position, @NonNull Contacts model) {

                profilePicRef=FirebaseDatabase.getInstance().getReference().child("profilePictures").child(getRef(position).getKey());
                profilePicRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String friendsProfilePicUrl=dataSnapshot.child("profilePic").getValue().toString();
                            Picasso.get().load(friendsProfilePicUrl).resize(150,150).into(holder.contactProfilePicture);
                        }
                        else{
                            holder.contactProfilePicture.setImageResource(R.drawable.usernamelogo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.name.setText(model.getName());
                holder.phoneNumber.setText(model.getPhoneNumber());
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visitorUserId=getRef(position).getKey();
                        gotoChatActivity(visitorUserId);
                    }
                });

            }

            @NonNull
            @Override
            public addContactsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                addContactsView viewHolder=new addContactsView(view);
                return viewHolder;

            }
        };
        findFriendRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    private void gotoChatActivity(String visitorUserId) {
        Intent gotoChatActivity =new Intent(Findfriend.this,ChatActvity.class);
        gotoChatActivity.putExtra("visitorUserId",visitorUserId);
        startActivity(gotoChatActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriend);
        InitializeFields();
        findFriendRecyclerList=findViewById(R.id.FindFriendsRecyclerLayout);
        findFriendRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        rootref= FirebaseDatabase.getInstance().getReference().child("users");


    }

    private void InitializeFields() {
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        if(currentuser==null){
            gotoStartActivity();
        }
        else{
            currentUserId=currentuser.getUid();
        }

    }

    private void gotoStartActivity() {
        Intent gotoStartActivity= new Intent(Findfriend.this,StartActivity.class);
        startActivity(gotoStartActivity);
    }
    public class addContactsView extends RecyclerView.ViewHolder{
        public TextView name,phoneNumber;
        public ImageView contactProfilePicture;
        public addContactsView(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userNameTextView);
            phoneNumber=itemView.findViewById(R.id.userPhoneNumberTextView);
            contactProfilePicture=itemView.findViewById(R.id.contactsProfilePicImageView);
        }
    }
}



