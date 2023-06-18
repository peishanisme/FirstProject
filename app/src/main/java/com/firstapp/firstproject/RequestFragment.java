package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firstapp.firstproject.R;
import com.firstapp.firstproject.adapter.RequestAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {

    private RecyclerView requestRecyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseReference friendsRef;
    private DatabaseReference usersRef;
    private String currentUserId;
    private List<User> requestList = new ArrayList<>();

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_tab_, container, false);
        InteractionTracker.add("Request");

        // Initialize RecyclerView
        requestRecyclerView = view.findViewById(R.id.rv_request_list);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestAdapter = new RequestAdapter(requestList, getActivity());
        requestRecyclerView.setAdapter(requestAdapter);

        // Get the current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Retrieve the friend request list of the current user from the database
        retrieveFriendRequest();

        return view;
    }

    private void retrieveFriendRequest() {

        // Get the reference to the FriendRequests node for the current user
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests").child(currentUserId);

        friendRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot friendRequestSnapshot : dataSnapshot.getChildren()) {
                    // Get the friend request UID and request type
                    String friendRequestUid = friendRequestSnapshot.getKey();
                    String request_type = friendRequestSnapshot.child("request_type").getValue(String.class);

                    if (request_type.equals("received")) {
                        // If the request type is "received", retrieve the user information
                        usersRef.child(friendRequestUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User request = dataSnapshot.getValue(User.class);
                                requestList.add(request);
                                requestAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
}