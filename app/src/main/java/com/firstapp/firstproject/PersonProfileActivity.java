/**package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonProfileActivity extends AppCompatActivity {

    private TextView username, email, phone, occupation, gender;
    private Button SendFriendReqButton, DeclineFriendReqButton;
    private DatabaseReference FriendRequestRef, userRef, FriendsRef;
    private FirebaseAuth mAuth;
    private String senderUserId, receiverUserId, CURRENT_STATE, saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);

        mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        InitializeFields();

        userRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String myProfileImage = snapshot.child("ProfilePicture").getValue().toString();
                    String myUserName = snapshot.child("UserName").getValue().toString();
                    String myUserEmail = snapshot.child("UserEmail").getValue().toString();
                    String myUserPhoneNumber = snapshot.child("UserPhoneNumber").getValue().toString();
                    String myOccupation = snapshot.child("Occupation").getValue().toString();
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

        if(!senderUserId.equals(receiverUserId)){
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
        FriendsRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendsRef.child(receiverUserId).child(senderUserId)
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

        FriendsRef.child(senderUserId).child(receiverUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendsRef.child(receiverUserId).child(senderUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FriendRequestRef.child(senderUserId).child(receiverUserId)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FriendRequestRef.child(receiverUserId).child(senderUserId)
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
        FriendRequestRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(receiverUserId).child(senderUserId)
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


        FriendRequestRef.child(senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(receiverUserId)){
                            String request_type = snapshot.child(receiverUserId).child("request_type").getValue().toString();

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
                                FriendsRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(receiverUserId)){
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
        FriendRequestRef.child(senderUserId).child(receiverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(receiverUserId).child(senderUserId)
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

    private void InitializeFields() {
        username = findViewById(R.id.username);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);
        SendFriendReqButton = (Button) findViewById(R.id.send_friend_request);
        DeclineFriendReqButton = (Button) findViewById(R.id.decline_friend_request);
        CURRENT_STATE = "not_friends";
    }
}