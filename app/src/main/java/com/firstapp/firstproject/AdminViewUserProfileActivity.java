package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firstapp.firstproject.adapter.UserAdapter;
import com.firstapp.firstproject.adapter.addhobbyAdapter;
import com.firstapp.firstproject.adapter.addjobAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Stack;

public class AdminViewUserProfileActivity extends AppCompatActivity {


    RecyclerView recyclerViewHobby;
    ArrayList<String> hobbies;
    com.firstapp.firstproject.adapter.addhobbyAdapter addhobbyAdapter;
    RecyclerView recyclerViewJob;
    Stack<String> jobStack;
    com.firstapp.firstproject.adapter.addjobAdapter addjobAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user_profile);

        TextView username = findViewById(R.id.UsernameDisplay);
        TextView fullname = findViewById(R.id.fullnameDisplay);
        TextView email = findViewById(R.id.emailDisplay);
        TextView phoneNumber = findViewById(R.id.phDisplay);
        TextView birthday = findViewById(R.id.birthdayDisplay);
        TextView age = findViewById(R.id.ageDisplay);
        TextView country = findViewById(R.id.countryDisplay);
        TextView state = findViewById(R.id.stateDisplay);
        TextView occupation = findViewById(R.id.occpDisplay);
        TextView gender = findViewById(R.id.genderDisplay);
        TextView relationship = findViewById(R.id.relationshipDisplay);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference hobbyReference = FirebaseDatabase.getInstance().getReference("Hobbies");
        String uid = getIntent().getStringExtra("uid");
        InteractionTracker.add("Profile");

        hobbies = new ArrayList<>();

        recyclerViewHobby = findViewById(R.id.hobbies_recyclerview);
        addhobbyAdapter = new addhobbyAdapter(hobbies);
        recyclerViewHobby.setAdapter(addhobbyAdapter);
        recyclerViewHobby.setLayoutManager(new LinearLayoutManager(this));

        jobStack = new Stack<>();
        recyclerViewJob = findViewById(R.id.jobs_recyclerview);
        addjobAdapter = new addjobAdapter(jobStack);
        recyclerViewJob.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJob.setAdapter(addjobAdapter);

        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue(String.class));
                fullname.setText(snapshot.child("fullName").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phoneNumber.setText(snapshot.child("phone_number").getValue(String.class));
                birthday.setText(snapshot.child("birthday").getValue(String.class));
                age.setText(snapshot.child("age").getValue(String.class));
                country.setText(snapshot.child("countryName").getValue(String.class));
                state.setText(snapshot.child("stateName").getValue(String.class));
                occupation.setText(snapshot.child("occupation").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));
                relationship.setText(snapshot.child("relationship").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        hobbyReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hobbies.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String hobby = dataSnapshot.getValue(String.class);
                        hobbies.add(hobby);
                    }
                }

                addhobbyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        DatabaseReference jobReference = FirebaseDatabase.getInstance().getReference("Jobs");
        jobReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobStack.clear();
                if(snapshot.exists()) {
                    Stack<String> jobList = new Stack<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String job = dataSnapshot.getValue(String.class);
                        jobList.push(job);
                    }
                    for (int i = 0; i <= jobList.size(); i++) {
                        jobStack.push(jobList.pop());

                    }
                }

                addjobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });



    }

}