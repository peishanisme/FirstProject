package com.firstapp.firstproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        private FirebaseAuth mAuth;
        String currentUser;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

            recyclerView = view.findViewById(R.id.my_freind_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            friendList = new ArrayList<>();
            adapter = new FriendsAdapter(friendList, getActivity());
            recyclerView.setAdapter(adapter);

            mAuth=FirebaseAuth.getInstance();
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            currentUser = mAuth.getCurrentUser().getUid();

            // Retrieve the friend list of the current user from the database
            retrieveFriendList();

            return view;
        }

        private void retrieveFriendList() {
            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends")
                    .child(currentUser);

            friendsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    friendList.clear();

                    for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        String friendUid = friendSnapshot.getKey();

                        usersRef.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User friend = dataSnapshot.getValue(User.class);

                                // Retrieve the user information from the friend DataSnapshot
                                String username = friendSnapshot.child("username").getValue(String.class);
                                String fullName = friendSnapshot.child("fullName").getValue(String.class);
                                String email = friendSnapshot.child("email").getValue(String.class);
                                String phone_number = friendSnapshot.child("phone_number").getValue(String.class);
                                // Add more fields as needed

                                // Set the retrieved information to the User object
                                friend.setUsername(username);
                                friend.setFullName(fullName);
                                friend.setEmail(email);
                                friend.setPhone_number(phone_number);
                                // Set more fields as needed

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