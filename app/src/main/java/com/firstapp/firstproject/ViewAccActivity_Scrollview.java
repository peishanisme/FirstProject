package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.firstproject.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewAccActivity_Scrollview extends AppCompatActivity {

    String received_id;

    private TextView username, email, phone, occupation, gender, mutualFriends, hobby, degreeConnection;
//private Button addFriendBtn;

    private Button SendFriendReqButton, DeclineFriendReqButton;


//private FirebaseAuth auth = FirebaseAuth.getInstance();
//private FirebaseUser currentUserF = auth.getCurrentUser();

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference selectedUserRef;
    private DatabaseReference currentUserRef;

    private String selectedUserId, currentUserId, CURRENT_STATE, saveCurrentDate;
    private DatabaseReference FriendRequestRef, FriendsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        received_id = getIntent().getStringExtra("uid");
//        selectedUserId = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");


        username = findViewById(R.id.usernameDisplay);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);
        mutualFriends = findViewById(R.id.mutualFriends);
        hobby = findViewById(R.id.hobbiesDisplay);
        degreeConnection = findViewById(R.id.degConnectionDisplay);
        SendFriendReqButton = findViewById(R.id.send_friend_request);
        DeclineFriendReqButton = findViewById(R.id.decline_friend_request);
        CURRENT_STATE = "not_friends";




        // Retrieve selected user's ID passed from the previous activity
        selectedUserId = getIntent().getStringExtra("uid");
//        currentUserId = getIntent().getStringExtra("currentUserId");
        // Get reference to the  selected user by ID
        selectedUserRef = userRef.child(selectedUserId);
        currentUserRef = userRef.child(currentUserId);


        Toast.makeText(this, "User ID: "+ selectedUserId, Toast.LENGTH_SHORT).show();


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
                    SendFriendReqButton.setVisibility(View.GONE);

                }else if(currentUser.getDegreeConnection(selectedUser) == 2){
                    // other connection, retrieve limited information
                    displayPartialProfile(selectedUser);
                    mutualFriends.setText(currentUser.getMutualFriendsNum(selectedUser) + " Mutual Friends");
                    degreeConnection.setText("2nd degree connection");
                    SendFriendReqButton.setVisibility(View.VISIBLE);

                }else if(currentUser.getDegreeConnection(selectedUser) == 3){
                    // other connection, retrieve limited information
                    displayPartialProfile(selectedUser);
                    mutualFriends.setText(currentUser.getMutualFriendsNum(selectedUser) + " Mutual Friends");
                    degreeConnection.setText("3rd degree connection");
                    SendFriendReqButton.setVisibility(View.VISIBLE);
                }else{
                    displayPartialProfile(selectedUser);
                }

                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        userRef.child(selectedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

//                    String myProfileImage = snapshot.child("ProfilePicture").getValue().toString();
                    String myUserName = snapshot.child("username").getValue().toString();
                    String myUserEmail = snapshot.child("email").getValue().toString();
                    String myUserPhoneNumber = snapshot.child("phone_number").getValue().toString();
                    String myOccupation = snapshot.child("occupation").getValue().toString();
                    String myGender = snapshot.child("gender").getValue().toString();

                    // Picasso.with(ctx).load(profilePicture).placeholder(R.drawable.);

//
//                    // Display the user's profile data in the UI
                    username.setText("@"+myUserName);
                    email.setText(myUserEmail);
                    phone.setText(myUserPhoneNumber);
                    occupation.setText(myOccupation);
                    gender.setText(myGender);

                    MaintananceofButtion();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        DeclineFriendReqButton.setVisibility(View.INVISIBLE);
        DeclineFriendReqButton.setEnabled(false);

        if(!currentUserId.equals(selectedUserId)){
            SendFriendReqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendFriendReqButton.setEnabled(false);
                    if(CURRENT_STATE.equals("not_friends")){
                        SendFriendRequestToaPerson();
                    }
                    if(CURRENT_STATE.equals("request_sent")){
                        CancelFriendRequest();
                    }
                    if(CURRENT_STATE.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(CURRENT_STATE.equals("friend")){
                        UnfriendAnExistingFriend();
                    }
                }
            });
        }else{
            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
            SendFriendReqButton.setVisibility(View.INVISIBLE);
        }

    }

    private void UnfriendAnExistingFriend() {
        FriendsRef.child(currentUserId).child(selectedUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendsRef.child(selectedUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendFriendReqButton.setEnabled(true);
                                                CURRENT_STATE = "not_friend";
                                                SendFriendReqButton.setText("Send Friend Request");

                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptFriendRequest() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        FriendsRef.child(currentUserId).child(selectedUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendsRef.child(selectedUserId).child(currentUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FriendRequestRef.child(currentUserId).child(selectedUserId)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FriendRequestRef.child(selectedUserId).child(currentUserId)
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        SendFriendReqButton.setEnabled(true);
                                                                        CURRENT_STATE = "friend";
                                                                        SendFriendReqButton.setText("Unfriend this Person");

                                                                        DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                                        DeclineFriendReqButton.setEnabled(false);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }

                        }
                    });
                }

            }
        });

    }

    private void CancelFriendRequest() {
        FriendRequestRef.child(currentUserId).child(selectedUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(selectedUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendFriendReqButton.setEnabled(true);
                                                CURRENT_STATE = "not_friend";
                                                SendFriendReqButton.setText("Send Friend Request");

                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void MaintananceofButtion() {


        FriendRequestRef.child(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(selectedUserId)){
                            String request_type = snapshot.child(selectedUserId).child("request_type").getValue().toString();

                            if(request_type.equals("sent")){
                                CURRENT_STATE = "request_sent";
                                SendFriendReqButton.setText("Cancel Friend Request");

                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                DeclineFriendReqButton.setEnabled(false);
                            } else if (request_type.equals("received")) {
                                CURRENT_STATE = "request_received";
                                SendFriendReqButton.setText("Accept Friend Request");

                                DeclineFriendReqButton.setVisibility(View.VISIBLE);
                                DeclineFriendReqButton.setEnabled(true);

                                DeclineFriendReqButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelFriendRequest();
                                    }
                                });

                            }
                            else {
                                FriendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(selectedUserId)){
                                            CURRENT_STATE = "friends";
                                            SendFriendReqButton.setText("Unfriend this Person");

                                            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                            DeclineFriendReqButton.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SendFriendRequestToaPerson() {
        FriendRequestRef.child(currentUserId).child(selectedUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(selectedUserId).child(currentUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendFriendReqButton.setEnabled(true);
                                                CURRENT_STATE = "request_sent";
                                                SendFriendReqButton.setText("Cancel Friend Request");

                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }





    //button function to send friend request
    //SendFriendReqButton.setOnClickListener(new View.OnClickListener() {
    //@Override
    //public void onClick(View v) {
    //   addFriendRequest(User otherUser);
    //   SendFriendReqButton.setText("Request Sent");
    // SendFriendReqButton.setEnabled(false);
    //}
    // });

    //  }



    public void displayFullProfile(User selectedUser){

        username.setText(selectedUser.getUsername());
        email.setText(String.valueOf(selectedUser.getEmail()));
        phone.setText(String.valueOf(selectedUser.getPhone_number()));
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
        email.setText(String.valueOf(selectedUser.getEmail()));
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