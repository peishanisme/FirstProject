package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.adapter.SearchFriendAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchForFriends_Fragment extends Fragment {
    private EditText searchBoxInput;
    private ImageButton searchButton;
    private RecyclerView searchResultList;
    private SearchFriendAdapter searchFriendAdapter;
    private List<User> userList;
    private DatabaseReference usersRef;
    String currentUserUid;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seacrh_for_friends_, container, false);
        InteractionTracker.add("Search Friends");

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize UI components
        searchBoxInput = view.findViewById(R.id.search_box_input);
        searchButton = view.findViewById(R.id.search_people_friends_button);
        searchResultList = view.findViewById(R.id.search_result_list);

        // Set up RecyclerView
        userList = new ArrayList<>();
        searchFriendAdapter = new SearchFriendAdapter(userList);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultList.setAdapter(searchFriendAdapter);

        // Set up click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBoxInput.getText().toString().trim();
                searchUsers(query);
            }
        });

        return view;
    }



    private void searchUsers(String query) {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (!currentUserUid.equals(user.getUid())) {
                        if (user.getUsername().toLowerCase().contains(query.toLowerCase()) || user.getEmail().toLowerCase().contains(query.toLowerCase())||user.getUid().toLowerCase().contains(query.toLowerCase())||user.getFullName().toLowerCase().contains(query.toLowerCase())||user.getPhone_number().contains(query)) {
                            userList.add(user);
                        }
                    }
                }
                // Sort userList based on username
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.getUsername().compareToIgnoreCase(user2.getUsername());
                    }
                });
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


