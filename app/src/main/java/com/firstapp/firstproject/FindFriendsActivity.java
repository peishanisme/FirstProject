package com.firstapp.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firstapp.firstproject.entity.FindFriends;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FindFriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView SearchResultList;
    private DatabaseReference UsersRef;
    private ImageButton searchButton;
    private EditText searchInputText;
    private FirebaseAuth mAuth;

    private FirebaseRecyclerAdapter<FindFriends, FindFriendViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
        searchInputText = (EditText) findViewById(R.id.search_box_input);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = searchInputText.getText().toString();
                SearchPeopleAndFriends(searchBoxInput);
            }
        });


    }

    private void SearchPeopleAndFriends(String searchBoxInput) {
        //Toast.makeText(, "Searching", Toast.LENGTH_LONG).show();

        //Query searchPeopleandFriendsQuery = UsersRef.orderByChild("fullName")
         //       .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        Query searchPeopleandFriendsQuery = UsersRef.orderByChild("fullName").equalTo(searchBoxInput);

        FirebaseRecyclerOptions<FindFriends> options =
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(searchPeopleandFriendsQuery, FindFriends.class).build();

        adapter
                =new FirebaseRecyclerAdapter<FindFriends, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull FindFriends model) {
                holder.userName.setText(model.getFullName());
                holder.userOccupation.setText(model.getOccupation());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPosition = holder.getAdapterPosition();
                        if (clickedPosition != RecyclerView.NO_POSITION) {
                            String visit_user_id = getRef(clickedPosition).getKey();
                            Intent profileIntent = new Intent(FindFriendsActivity.this, ViewAccActivity_Scrollview.class);
                            profileIntent.putExtra("visit_user_id", visit_user_id);
                            startActivity(profileIntent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return viewHolder;
            }

        };
        SearchResultList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override

   public void onStop(){
       super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userOccupation;
        View mView;

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
            public void setFullName(String fullName) {
            userName = (TextView) mView.findViewById(R.id.all_user_profile_full_name);
            userName.setText(fullName);

        }

        public void setOccupation(String occupation) {
            userOccupation = (TextView) mView.findViewById(R.id.all_user_occupation);
            userOccupation.setText(occupation);
         }

    }
}


