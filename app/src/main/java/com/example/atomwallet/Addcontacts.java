package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Addcontacts extends AppCompatActivity {
    RecyclerView addContactRecyclerList;
    DatabaseReference rootref,profilePicRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options= new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(rootref,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts,addContactView> adapter=new FirebaseRecyclerAdapter<Contacts, addContactView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final addContactView holder, final int position, @NonNull Contacts model) {
                String uName,uPhoneNumber;


                uName=model.getName();
                uPhoneNumber=model.getPhoneNumber();

                profilePicRef=FirebaseDatabase.getInstance().getReference().child("profilePictures").child(getRef(position).getKey());
                profilePicRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String friendsProfilePicUrl=dataSnapshot.child("profilePic").getValue().toString();
                            Picasso.get().load(friendsProfilePicUrl).resize(150,150).into(holder.contactsProfilePic);
                        }
                        else{
                            holder.contactsProfilePic.setImageResource(R.drawable.usernamelogo);
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
                        String visitorUserId;
                        visitorUserId=getRef(position).getKey();



                        if(currentUserId.equals(visitorUserId)){
                            Toast.makeText(Addcontacts.this, "Can't send money to yourself", Toast.LENGTH_SHORT).show();
                        }
                        else
                            gotoSendMoney(visitorUserId);

                    }
                });



            }

            @NonNull
            @Override
            public addContactView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                addContactView viewHolder =new addContactView(view);
                return viewHolder;
            }
        };
       addContactRecyclerList.setAdapter(adapter);
       adapter.startListening();
    }

    private void gotoStartActivity() {
        Intent gotoStartActivity= new Intent(Addcontacts.this,StartActivity.class);
        startActivity(gotoStartActivity);
    }

    private void gotoSendMoney(String visitorUserId) {

        Intent gotoSendMoney=new Intent(Addcontacts.this,sendmoney.class);
        gotoSendMoney.putExtra("visitorUserId",visitorUserId);
        startActivity(gotoSendMoney);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        initializeFields();
        addContactRecyclerList=findViewById(R.id.AddContactsRecyclerLayout);
        addContactRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        rootref= FirebaseDatabase.getInstance().getReference().child("users");

    }

    private void initializeFields() {
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if(currentUser==null)
            gotoStartActivity();
        else
            currentUserId=currentUser.getUid();
    }

    public  class addContactView extends RecyclerView.ViewHolder {
        public TextView name,phoneNumber;
        public ImageView contactsProfilePic;

        public addContactView(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userNameTextView);
            phoneNumber=itemView.findViewById(R.id.userPhoneNumberTextView);
            contactsProfilePic=itemView.findViewById(R.id.contactsProfilePicImageView);


        }
    }

}
