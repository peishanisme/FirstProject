package com.firstapp.firstproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.Nullable;
import com.firstapp.firstproject.adapter.FriendsAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

    public class FriendList_Fragment extends Fragment {

        private RecyclerView recyclerView;
        private FriendsAdapter adapter;
        private List<User> friendList;
        private DatabaseReference usersRef;
        DatabaseReference friendsRef;
        private FirebaseAuth mAuth;
        String currentUser;
        TextView numberOfFriends;
        int friendCount;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
            InteractionTracker.add("Friend list");

            friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
            recyclerView = view.findViewById(R.id.my_freind_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            friendList = new ArrayList<>();
            adapter = new FriendsAdapter(friendList, getActivity());
            recyclerView.setAdapter(adapter);

            mAuth=FirebaseAuth.getInstance();
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            currentUser = mAuth.getCurrentUser().getUid();

            numberOfFriends=view.findViewById(R.id.numberOfFriends);

            // Retrieve the friend list of the current user from the database
            retrieveFriendList();

            friendsRef.child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        friendCount=(int)snapshot.getChildrenCount();
                        numberOfFriends.setText("("+Integer.toString(friendCount)+")");

                    }else
                        numberOfFriends.setText("0");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return view;
        }

        private void retrieveFriendList() {

            friendsRef.child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    friendList.clear();

                    for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        String friendUid = friendSnapshot.getKey();

                        usersRef.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User friend = dataSnapshot.getValue(User.class);
                                friendList.add(friend);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error
                            }
                        });
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });
        }
    }