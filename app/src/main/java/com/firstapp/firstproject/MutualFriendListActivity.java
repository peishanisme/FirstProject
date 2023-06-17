package com.firstapp.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.adapter.FriendsAdapter;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MutualFriendListActivity extends AppCompatActivity {

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private List<User> friendList;
    private TextView numOfFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_friend_list);

        List<String> receivedList = getIntent().getStringArrayListExtra("mutualFriendList");

         numOfFriends = findViewById(R.id.numberOfMutualFriends);
         numOfFriends.setText("(" + receivedList.size() + ")");


        RecyclerView recyclerView = findViewById(R.id.my_mutual_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendList = new ArrayList<>();
        FriendsAdapter adapter = new FriendsAdapter(friendList,this);
        recyclerView.setAdapter(adapter);;

        // save user into list
        for(String mutualFriendID : receivedList){
            userRef.child(mutualFriendID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User friend = snapshot.getValue(User.class);
                    friendList.add(friend);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }



    }
}
