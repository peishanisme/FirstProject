package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAccActivity_Scrollview extends AppCompatActivity {

private TextView username, email, phone, occupation, gender, mutualFriends;
private Button addFriendBtn;
private boolean areFriend;
private FirebaseDatabase database= FirebaseDatabase.getInstance();
private FirebaseAuth auth = FirebaseAuth.getInstance();
private FirebaseUser currentUser = auth.getCurrentUser();

private DatabaseReference otherUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);
        mutualFriends = findViewById(R.id.mutualFriends);
        addFriendBtn = findViewById(R.id.addFriendBtn);

        // Retrieve selected user's ID passed from the previous activity
        String selectedUserId = getIntent().getStringExtra("selectedUserId");
        // Get reference to the  selected user by ID
        otherUserRef = database.getReference("Users").child(selectedUserId);


        // Retrieve the user's profile data from the database
        otherUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User selectedUser = snapshot.getValue(User.class);
                    displayProfile(currentUser, selectedUser);

                }
                else{
                    username.setText("User not found");
                    email.setText("");
                    phone.setText("");
                    occupation.setText("");
                    gender.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


        //button function to send friend request
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendRequest(User otherUser);
                addFriendBtn.setText("Request Sent");
                addFriendBtn.setEnabled(false);
            }
        });


        }


    public void displayProfile(FirebaseUser currentUser, User otherUser){
        int degreeConnection = getDegreeOfConnection(currentUser, otherUser);

        if(degreeConnection == 1){
            // First-degree connection, retrieve full profile information

            username.setText(selectedUser.getName());
            email.setText(String.valueOf(selectedUser.getEmail));
            phone.setText(String.valueOf(selectedUser.getPhone()));
            occupation.setText(selectedUser.getOccupation());
            gender.setText(selectedUser.getGender());
            mutualFriends.setText(currentUser.getMutualFriendsNum(otherUser) + " Mutual Friends");

            addFriendBtn.setVisibility(View.GONE); // Hide the button if they are already friends

        }else{
            // other connection, retrieve limited information
            username.setText(selectedUser.getName());
            email.setText(String.valueOf(selectedUser.getEmail));
            occupation.setText("-");
            gender.setText("-");
            mutualFriends.setText(currentUser.getMutualFriendsNum(otherUser) + " Mutual Friends");

            addFriendBtn.setVisibility(View.VISIBLE); // Show the button if they are not friends
        }
    }



}