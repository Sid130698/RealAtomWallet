package com.example.atomwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Addcontacts extends AppCompatActivity {
    RecyclerView addContactRecyclerList;
    DatabaseReference rootref;
    FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options= new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(rootref,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts,addContactView> adapter=new FirebaseRecyclerAdapter<Contacts, addContactView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull addContactView holder, final int position, @NonNull Contacts model) {
                String uName,uPhoneNumber;
                uName=model.getName();
                uPhoneNumber=model.getPhoneNumber();
                Log.d("naam",model.getName());
                holder.name.setText(model.getName());
                Log.d("ringring",model.getPhoneNumber());
                holder.phoneNumber.setText(model.getPhoneNumber());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId;
                        userId=getRef(position).getKey();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        addContactRecyclerList=findViewById(R.id.AddContactsRecyclerLayout);
        addContactRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        rootref= FirebaseDatabase.getInstance().getReference().child("users");

    }

    public  class addContactView extends RecyclerView.ViewHolder {
        public TextView name,phoneNumber;

        public addContactView(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userNameTextView);
            phoneNumber=itemView.findViewById(R.id.userPhoneNumberTextView);
        }
    }

}
