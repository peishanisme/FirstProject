/**package com.firstapp.firstproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;



// display friends list function but not use this dy
public class FriendActivity extends AppCompatActivity {

    private RecyclerView myFriendList;
    private DatabaseReference FriendsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String online_user_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child("online_user_id");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myFriendList = (RecyclerView) findViewById(R.id.friends_list);
        myFriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendList.setLayoutManager(linearLayoutManager);

        DisplayAllFriends();
    }

    private void DisplayAllFriends() {
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                        Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        FriendsRef
        ){

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;

            }

            @Override
            protected void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull Friends model) {

            }

            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position){
                viewHolder.setDate(model.getDate());
                final String usersIDs = getRef(position).getKey();

                UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final String UserName = snapshot.child("UserName").getValue().toString();
                            //final String ProfilePicture = snapshot.child("ProfilePicture").getValue().toString();

                            viewHolder.setFullName(UserName);
                            //viewHolder.setProfilePicture(getApplicationContext(), ProfilePicture);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            };
        };
        myFriendList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        //public void setProfilePicture(Context ctx, String profilePicture) {
          //  CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            //Picasso.with(ctx).load(profilePicture).placeholder(R.drawable.profile).into(myImage);

        //}

        public void setFullName(String fullName) {
            TextView myName = (TextView) mView.findViewById(R.id.all_user_profile_full_name);
            myName.setText(fullName);
        }

        public void setDate(String date) {
            TextView friendsDate = (TextView) mView.findViewById(R.id.all_user_status);
            friendsDate.setText("Friends Since: "+ date);
        }
    }
}