package com.firstapp.firstproject;

import android.os.Bundle;
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

public class ViewAccActivity_Scrollview extends AppCompatActivity {

    private TextView username, email, phone, occupation, gender, country, birthday, relationship,friends, mutualFriends,mutual1, mutual2, mutual3, mutual4, degreeConnection;
    TextView[] mutualName = {mutual1, mutual2, mutual3, mutual4};
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
    private DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("Friends");
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
        mutualFriends = findViewById(R.id.mutualFriends);
        mutual1 = findViewById(R.id.textView);
        mutual2 = findViewById(R.id.textView2);
        mutual3 = findViewById(R.id.textView3);
        mutual4 = findViewById(R.id.textView4);
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

        List<String> currentUserFriendList = new ArrayList<>();
        List<String> selectedUserFriendList = new ArrayList<>();

        Toast.makeText(this, "User ID: "+ selectedUserId, Toast.LENGTH_SHORT).show();

        // retrieve friend list of users
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get current user friend list
                for (DataSnapshot userSnapshot : dataSnapshot.child(currentUserId).getChildren()) {
                    String currentUserFriendId = userSnapshot.getValue().toString();

                    if (currentUserFriendId != null) {
                        currentUserFriendList.add(currentUserFriendId); // Add the user id to the list
                    }
                }

                // get selected user friend list
                for (DataSnapshot userSnapshot : dataSnapshot.child(selectedUserId).getChildren()) {
                    String selectedUserFriendId = userSnapshot.getValue().toString();

                    if (selectedUserFriendId != null) {
                        selectedUserFriendList.add(selectedUserFriendId);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // get mutual friend between two user
        List<String> mutualFriendList = getMutualFriendID(currentUserFriendList, selectedUserFriendList);

        if(mutualFriendList.isEmpty()){
            for(TextView tv : mutualName){
                tv.setVisibility(View.GONE);
            }
        }else{
            for(String mutualFriendID : mutualFriendList){
                userRef.child(mutualFriendID).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue(String.class);

                        for(int i=0; i<4; i++){
                            if(!username.isEmpty())
                                mutualName[i].setText(username);
                            else
                                mutualName[i].setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error if necessary
                    }
                });
            }

        }


        // retrieve selected user basic info
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
                        mutualFriends.setText(mutualFriendList.size() + " Mutual Friends");
                    }else{
                        mutualFriends.setText("No mutual friends");
                    }


                    if (currentUserFriendList.contains(selectedUserId)) {
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

                                Stack<String> jobList = new Stack<>();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String job = dataSnapshot.getValue(String.class);
                                    jobList.push(job);
                                }
                                for (int i = 0; i <= jobList.size(); i++) {
                                    jobStack.push(jobList.pop());

                                }

                                addjobAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });

                    } else {
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


    // Methods
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

/* retrieve user basic info old
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


*/





    public List<String> getMutualFriendID(List<String> currentUserFriend, List<String> selectedUserFriend){
        List<String> mutualFriends = new ArrayList<>();
        for (String friendID : currentUserFriend) {
            if (selectedUserFriend.contains(friendID)) {
                mutualFriends.add(friendID);
            }
        }
        return mutualFriends;
    }


/* display profile method (old)
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


*/
}