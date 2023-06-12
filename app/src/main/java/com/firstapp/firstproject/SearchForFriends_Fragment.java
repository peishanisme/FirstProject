package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.adapter.SearchFriendAdapter;
import com.firstapp.firstproject.entity.User;
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
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seacrh_for_friends_, container, false);

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
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query searchQuery = usersRef.orderByChild("username").startAt(query).endAt(query + "\uf8ff");
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null&&!(userList.contains(user))) {
                        userList.add(user);
                    }
                }
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        Query searchQuery2 = usersRef.orderByChild("fullName").startAt(query).endAt(query + "\uf8ff");
        searchQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null&&!(userList.contains(user))) {
                        if (!userList.contains(user)) {
                            userList.add(user);
                        }
                    }
                }
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        Query searchQuery3 = usersRef.orderByChild("email").startAt(query).endAt(query + "\uf8ff");
        searchQuery3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null&&!(userList.contains(user))) {
                        if (!userList.contains(user)) {
                            userList.add(user);
                        }
                    }
                }
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        Query searchQuery4 = usersRef.orderByChild("phone_number").startAt(query).endAt(query + "\uf8ff");
        searchQuery4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null&&!(userList.contains(user))) {
                        if (!userList.contains(user)) {
                            userList.add(user);
                        }
                    }
                }
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        Query searchQuery5 = usersRef.orderByChild("useruid").startAt(query).endAt(query + "\uf8ff");
        searchQuery5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null&&!(userList.contains(user))) {
                        if (!userList.contains(user)) {
                            userList.add(user);
                        }
                    }
                }
                searchFriendAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }


}


