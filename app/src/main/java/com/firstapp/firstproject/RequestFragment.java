package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firstapp.firstproject.R;
import com.firstapp.firstproject.adapter.RequestAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RequestFragment extends Fragment {

    private RecyclerView requestRecyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseReference friendRequestsRef;
    private DatabaseReference friendsRef;
    private DatabaseReference usersRef;
    private String currentUserId;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_tab_, container, false);

        // Initialize RecyclerView
        requestRecyclerView = view.findViewById(R.id.rv_request_list);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRecyclerView.setHasFixedSize(true);

        // Get the current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the Firebase references
        friendRequestsRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Query the friend requests for the current user
        Query query = friendRequestsRef.child(currentUserId);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        // Initialize and set the adapter
        requestAdapter = new RequestAdapter(options.getSnapshots(), friendRequestsRef, friendsRef, usersRef, currentUserId);
        requestRecyclerView.setAdapter(requestAdapter);

        return view;
    }


}
