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

import java.util.List;

public class ViewAccActivity_Scrollview extends AppCompatActivity {

private TextView username, email, phone, occupation, gender, mutualFriends, hobby, degreeConnection;
private Button addFriendBtn;


//private FirebaseAuth auth = FirebaseAuth.getInstance();
//private FirebaseUser currentUserF = auth.getCurrentUser();

private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
private DatabaseReference selectedUserRef;
private DatabaseReference currentUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);

        username = findViewById(R.id.usernameDisplay);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);
        mutualFriends = findViewById(R.id.mutualFriends);
        hobby = findViewById(R.id.hobbiesDisplay);
        degreeConnection = findViewById(R.id.degConnectionDisplay);
        addFriendBtn = findViewById(R.id.addFriendBtn);


        // Retrieve selected user's ID passed from the previous activity
        String selectedUserId = getIntent().getStringExtra("selectedUserId");
        String currentUserId = getIntent().getStringExtra("currentUserId");
        // Get reference to the  selected user by ID
        selectedUserRef = userRef.child(selectedUserId);
        currentUserRef = userRef.child(currentUserId);




        // Retrieve the user's profile data from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User selectedUser = snapshot.child(selectedUserId).getValue(User.class);
                User currentUser = snapshot.child(currentUserId).getValue(User.class);

                if(currentUser.getDegreeConnection(selectedUser) == 1){
                    // First-degree connection, retrieve full profile information
                    displayFullProfile(selectedUser);
                    mutualFriends.setText(currentUser.getMutualFriendsNum(selectedUser) + " Mutual Friends");
                    degreeConnection.setText("1st degree connection");
                    addFriendBtn.setVisibility(View.GONE);

                }else if(currentUser.getDegreeConnection(selectedUser) == 2){
                    // other connection, retrieve limited information
                    displayPartialProfile(selectedUser);
                    mutualFriends.setText(currentUser.getMutualFriendsNum(selectedUser) + " Mutual Friends");
                    degreeConnection.setText("2nd degree connection");
                    addFriendBtn.setVisibility(View.VISIBLE);

                }else if(currentUser.getDegreeConnection(selectedUser) == 3){
                    // other connection, retrieve limited information
                    displayPartialProfile(selectedUser);
                    mutualFriends.setText(currentUser.getMutualFriendsNum(selectedUser) + " Mutual Friends");
                    degreeConnection.setText("3rd degree connection");
                    addFriendBtn.setVisibility(View.VISIBLE);
                }else{
                    displayPartialProfile(selectedUser);
                }

                return null;
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



    public void displayFullProfile(User selectedUser){

            username.setText(selectedUser.getUsername());
            email.setText(String.valueOf(selectedUser.getEmail()));
            phone.setText(String.valueOf(selectedUser.getPhone_number());
            occupation.setText(selectedUser.getOccupation());
            gender.setText(selectedUser.getGender());

            List<String> hobbyList = selectedUser.getHobbies();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hobbyList.size(); i++) {
                sb.append(hobbyList.get(i));
                if (i != hobbyList.size() - 1) {
                    sb.append(", ");
                }
            }
            hobby.setText(sb.toString());


    }

    public void displayPartialProfile(User selectedUser){

            username.setText(selectedUser.getUsername());
            email.setText(String.valueOf(selectedUser.getEmail());
            phone.setText("-");
            occupation.setText("-");
            gender.setText("-");
            hobby.setText("-");

    }

    public void displayUserNotFound(){
        username.setText("User not found");
        email.setText("");
        phone.setText("");
        occupation.setText("");
        gender.setText("");
    }

}