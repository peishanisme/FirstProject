package com.firstapp.firstproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.adapter.addhobbyAdapter;
import com.firstapp.firstproject.adapter.addjobAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAccActivity_Scrollview extends AppCompatActivity {

    private TextView username, email, phone, occupation, gender, country, birthday, relationship, friends,mutual1, mutual2, mutual3,  mutualFriends, degreeConnection;
    CircleImageView friendPic1, friendPic2, friendPic3;
    RecyclerView recyclerViewHobby;
    ArrayList<String> hobbies;
    addhobbyAdapter addhobbyAdapter;
    RecyclerView recyclerViewJob;
    Stack<String> jobStack;
    addjobAdapter addjobAdapter;
    private String selectedUserId, currentUserId, CURRENT_STATE, saveCurrentDate;

    private Button SendFriendReqButton, DeclineFriendReqButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference selectedUserRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
    private DatabaseReference FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");;
    private DatabaseReference hobbyReference = FirebaseDatabase.getInstance().getReference("Hobbies");
    private DatabaseReference jobReference = FirebaseDatabase.getInstance().getReference("Jobs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);


        // Retrieve selected user's ID passed from the previous activity
        currentUserId = mAuth.getCurrentUser().getUid();
        selectedUserId = getIntent().getStringExtra("uid");

        // Get reference to the  selected user by ID
        selectedUserRef = userRef.child(selectedUserId);
        currentUserRef = userRef.child(currentUserId);


        username = findViewById(R.id.usernameDisplay);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);
        country = findViewById(R.id.countryDisplay);
        birthday = findViewById(R.id.birthdayDisplay);
        relationship = findViewById(R.id.relationshipDisplay);
        friends = findViewById(R.id.Friends);
        mutual1 = findViewById(R.id.textView);
        mutual2 = findViewById(R.id.textView3);
        mutual3 = findViewById(R.id.textView2);
        friendPic1 = findViewById(R.id.friend_picture);
        friendPic2 = findViewById(R.id.friend_picture2);
        friendPic3 = findViewById(R.id.friend_picture3);
        mutualFriends = findViewById(R.id.mutualFriends);
        degreeConnection = findViewById(R.id.degConnectionDisplay);
        SendFriendReqButton = findViewById(R.id.send_friend_request);
        DeclineFriendReqButton = findViewById(R.id.decline_friend_request);
        CURRENT_STATE = "not_friends";


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


        // create list to store user's friend list
        List<String> currentUserFriendList = new ArrayList<>();
        List<String> selectedUserFriendList = new ArrayList<>();
        Toast.makeText(this, "User ID: "+ selectedUserId, Toast.LENGTH_SHORT).show();

        // retrieve friend list of users
        FriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get current user friend list
                DataSnapshot currentUserSnapshot = dataSnapshot.child(currentUserId);
                if (currentUserSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : currentUserSnapshot.getChildren()) {
                        String currentUserFriendId = userSnapshot.getKey();
                        currentUserFriendList.add(currentUserFriendId);
                    }
                }

                // Get selected user friend list
                DataSnapshot selectedUserSnapshot = dataSnapshot.child(selectedUserId);
                if (selectedUserSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : selectedUserSnapshot.getChildren()) {
                        String selectedUserFriendId = userSnapshot.getKey();
                        selectedUserFriendList.add(selectedUserFriendId);
                    }
                }

                Log.d("FriendList", "Current User Friend List: " + currentUserFriendList);
                Log.d("FriendList", "Selected User Friend List: " + selectedUserFriendList);


                // get mutual friend between two user
                List<String> mutualFriendList = getMutualFriendID(currentUserFriendList, selectedUserFriendList);
                Log.d("FriendList", "MutualFriend List: " + mutualFriendList);

                // set textview and imageview for mutual friends
                TextView[] mutualName = {mutual1, mutual2, mutual3};
                CircleImageView[] friendPics = {friendPic1, friendPic2, friendPic3};

                if(mutualFriendList.isEmpty()){
                    for(TextView tv : mutualName){
                        tv.setVisibility(View.GONE);
                    }

                    for(CircleImageView iv : friendPics){
                        iv.setVisibility(View.GONE);
                    }

                }else{
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int mutualFriendCount = Math.min(mutualFriendList.size(), 3);
                            for(int i = 0; i < mutualFriendCount; i++){
                                String mutualFriendID = mutualFriendList.get(i);
                                if(mutualFriendID != null){
                                    DataSnapshot mutualFriendSnapshot = dataSnapshot.child(mutualFriendID);
                                    if(dataSnapshot.exists()){
                                        String mutualFriendUsername = mutualFriendSnapshot.child("username").getValue().toString();
                                        mutualName[i].setText(mutualFriendUsername);
                                        mutualName[i].setVisibility(View.VISIBLE);
                                        friendPics[i].setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error if necessary
                        }
                    });


                }


                selectedUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String selectedUsername = snapshot.child("username").getValue().toString();
                            String selectedUserEmail = snapshot.child("email").getValue().toString();
                            String selectedUserPhoneNumber = snapshot.child("phone_number").getValue().toString();
                            String selectedOccupation = snapshot.child("occupation").getValue().toString();
                            String selectedGender = snapshot.child("gender").getValue().toString();
                            String selectedCountry = snapshot.child("countryName").getValue().toString();
                            String selectedBirthday = snapshot.child("birthday").getValue().toString();
                            String selectedRelationship = snapshot.child("relationship").getValue().toString();



                            // Display the user's profile data in the UI
                            username.setText(selectedUsername);
                            email.setText(selectedUserEmail);
                            friends.setText("Friends (" + selectedUserFriendList.size() + ")");

                            if(!mutualFriendList.isEmpty()){
                                mutualFriends.setText(String.format("%d Mutual Friends", mutualFriendList.size()));
                            }else{
                                mutualFriends.setText("No mutual friends");
                            }

                            if(currentUserFriendList.contains(selectedUserId)){

                                phone.setText(selectedUserPhoneNumber);
                                occupation.setText(selectedOccupation);
                                gender.setText(selectedGender);
                                country.setText(selectedCountry);
                                birthday.setText(selectedBirthday);
                                relationship.setText(selectedRelationship);

                                hobbyReference.child(selectedUserId).addValueEventListener(new ValueEventListener() {
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


                                jobReference.child(selectedUserId).addValueEventListener(new ValueEventListener() {
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

                            }else{
                                phone.setText("-");
                                occupation.setText("-");
                                gender.setText("-");
                                country.setText("-");
                                birthday.setText("-");
                                relationship.setText("-");
                            }


                            MaintananceofButtion();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Failed to read friendlist: " + error.getMessage());
            }
        });



        DeclineFriendReqButton.setVisibility(View.INVISIBLE);
        DeclineFriendReqButton.setEnabled(false);

        if(!currentUserId.equals(selectedUserId)){
            FriendRequestRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(selectedUserId)) {
                        if (snapshot.child(selectedUserId).child("request_type").getValue().equals("sent")) {
                            CURRENT_STATE = "request_sent";
                            SendFriendReqButton.setText("Cancel Friend Request");
                            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                            DeclineFriendReqButton.setEnabled(false);
                        } else if (snapshot.child(selectedUserId).child("request_type").getValue().equals("received")) {
                            CURRENT_STATE = "request_received";
                            SendFriendReqButton.setText("Accept Friend Request");

                        }
                    }
                    else {
                        FriendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(selectedUserId)) {
                                    CURRENT_STATE = "friend";
                                    SendFriendReqButton.setText("Unfriend this Person");
                                    DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                    DeclineFriendReqButton.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
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

    public List<String> getMutualFriendID(List<String> currentUserFriend, List<String> selectedUserFriend){
        List<String> mutualFriends = new ArrayList<>();
        for (String friendID : selectedUserFriend) {
            if (currentUserFriend.contains(friendID)) {
                mutualFriends.add(friendID);
            }
        }
        return mutualFriends;
    }





}