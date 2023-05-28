package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAccActivity_Scrollview extends AppCompatActivity {

private TextView username, email, phone, occupation, gender;
private FirebaseDatabase database;
private DatabaseReference userRef;

    String emailview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc_scrollview);

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailDisplay);
        phone = findViewById(R.id.phDisplay);
        occupation = findViewById(R.id.occpDisplay);
        gender = findViewById(R.id.genderDisplay);

        // Retrieve the selected user's ID passed from the previous activity/fragment
        String selectedUserId = getIntent().getStringExtra("selectedUserId");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users").child(selectedUserId);


        // Retrieve the user's profile data from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User selectedUser = snapshot.getValue(User.class);

                    // Display the user's profile data in the UI
                    username.setText(selectedUser.getName());
                    email.setText(String.valueOf(selectedUser.getAge()));
                    phone.setText(String.valueOf(selectedUser.getPhone()));
                    occupation.setText(selectedUser.getOccupation());
                    gender.setText(selectedUser.getGender());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

    }


}