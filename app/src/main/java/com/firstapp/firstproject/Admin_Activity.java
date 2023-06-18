package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firstapp.firstproject.adapter.UserAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Activity extends AppCompatActivity implements UserAdapter.OnDeleteAccountClickListener {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ImageButton logout_button = findViewById(R.id.logout_button);


        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize the RecyclerView
        userRecyclerView = findViewById(R.id.user_recyclerview);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of users
        userList = new ArrayList<>();

        // Initialize the UserAdapter
        userAdapter = new UserAdapter(userList, this);
        userRecyclerView.setAdapter(userAdapter);

        // Retrieve user information from Firebase and add them to the userList
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // Clear the list before adding new data

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Get the UID of the user
                    String uid = userSnapshot.getKey();

                    // Retrieve the user information from the child nodes
                    String username = userSnapshot.child("username").getValue(String.class);
                    String fullName = userSnapshot.child("fullName").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String phone_number = userSnapshot.child("phone_number").getValue(String.class);

                    // Create a User object with the retrieved information
                    User user = new User(uid, username, fullName, email, phone_number);

                    // Add the user to the userList
                    userList.add(user);
                }

                userAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(Admin_Activity.this, Starting_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDeleteAccountClick(User user) {
        String uid = user.getUid();

        // Delete account from Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.child(uid).removeValue();
        DatabaseReference hobbyRef = FirebaseDatabase.getInstance().getReference("Hobbies");
        hobbyRef.child(uid).removeValue();
        DatabaseReference jobRef = FirebaseDatabase.getInstance().getReference("Jobs");
        jobRef.child(uid).removeValue();
        DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("Friends");
        friendRef.child(uid).removeValue();
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot friendSnapShot : snapshot.getChildren()) {
                    for (DataSnapshot eachUserFriendSnapshot : friendSnapShot.getChildren()) {
                        String friendUid = eachUserFriendSnapshot.getKey();
                        DatabaseReference specificFriendRef = friendRef.child(friendSnapShot.getKey()).child(friendUid);
                        if (friendUid.equals(uid))
                            specificFriendRef.child(uid).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("FriendRequests");
        requestRef.child(uid).removeValue();
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot friendRequestSnapShot : snapshot.getChildren()) {
                    for (DataSnapshot eachUserRequestSnapshot : friendRequestSnapShot.getChildren()) {
                        String requestUid = eachUserRequestSnapshot.getKey();
                        DatabaseReference specificReqRef = requestRef.child(friendRequestSnapShot.getKey());
                        if (requestUid.equals(uid))
                            specificReqRef.child(uid).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}

