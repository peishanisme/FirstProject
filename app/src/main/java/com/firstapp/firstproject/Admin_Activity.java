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

        // Used for user authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize the RecyclerView
        userRecyclerView = findViewById(R.id.user_recyclerview);
        // userRecyclerView is configured with a LinearLayoutManager
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of user to store User objects retrieved from Firebase
        userList = new ArrayList<>();

        // Initialize the UserAdapter which used to bind user data to the Recycler View and display a list of users
        userAdapter = new UserAdapter(userList, this);
        // Set the userAdapter as the adapter for the userRecyclerView
        userRecyclerView.setAdapter(userAdapter);

        // Retrieve user information from Firebase and add them to the userList
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Add event listener to usersRef to listen for changes in the data
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // Clear the list before adding new data

                // Iterates over the DataSnapshot children to retrieve user information
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

        // logout function
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out the user
                firebaseAuth.signOut();
                // navigate to the Starting_Activity
                Intent intent = new Intent(Admin_Activity.this, Starting_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // implemented from the UserAdapter.OnDeleteAccountClickListener interface
    // called when the delete account button is clicked for a specific user
    // delete the user account and related data from Firebase Realtime Database
    @Override
    public void onDeleteAccountClick(User user) {
        // Get the uid of the user to be deleted from the user parameter
        String uid = user.getUid();

        // Delete account from Realtime Database
        // Database references to the relevant nodes are created
        // User's account nad related data are removed using removeValue()
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.child(uid).removeValue();
        DatabaseReference hobbyRef = FirebaseDatabase.getInstance().getReference("Hobbies");
        hobbyRef.child(uid).removeValue();
        DatabaseReference jobRef = FirebaseDatabase.getInstance().getReference("Jobs");
        jobRef.child(uid).removeValue();
        DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("Friends");
        friendRef.child(uid).removeValue();

        // Adds a ValueEventListener to the friendRef DatabaseReference
        // Listener is triggered when the data at the "friendRef" location changes
        // The "onDataChange" method is called when the data changes
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterates over the snapshot.getChildren() to access the child snapshots of "friendRef"
                // Each child snapshot represents a specific user's friend list
                // friendSnapShot refers to these individual friend list snapshots
                for (DataSnapshot friendSnapShot : snapshot.getChildren()) {
                    // eachUserFriendSnapshot represent the individual friends within a specific user's friend list
                    // Iterates over each friend in the friendSnapShot
                    for (DataSnapshot eachUserFriendSnapshot : friendSnapShot.getChildren()) {
                        // Retrieves the friendUid of each friend
                        // friendUid is used to identify the specific friend within the friend list
                        String friendUid = eachUserFriendSnapshot.getKey();
                        // navigate to the location of the specific friend's data.
                        DatabaseReference specificFriendRef = friendRef.child(friendSnapShot.getKey()).child(friendUid);
                        if (friendUid.equals(uid)) //  if the friendUid is equal to the uid
                            specificFriendRef.child(uid).removeValue(); // remove the reference to the user being deleted from the friend's data
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // listen for changes in the friend requests data
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("FriendRequests");
        requestRef.child(uid).removeValue();
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // iterates over each child snapshot (friendRequestSnapShot) of the snapshot to access individual friend request lists
                for (DataSnapshot friendRequestSnapShot : snapshot.getChildren()) {
                    for (DataSnapshot eachUserRequestSnapshot : friendRequestSnapShot.getChildren()) {
                        String requestUid = eachUserRequestSnapshot.getKey();
                        // navigates to the location of the specific friend request list
                        DatabaseReference specificReqRef = requestRef.child(friendRequestSnapShot.getKey());
                        if (requestUid.equals(uid)) // checks if the requestUid is equal to the uid
                            specificReqRef.child(uid).removeValue(); //remove the reference to the user being deleted from that friend request
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}