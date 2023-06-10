package com.firstapp.firstproject;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment{
    private View root;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageButton edit_profile_button=root.findViewById(R.id.edit_profile_button);
        ImageButton logout_button=root.findViewById(R.id.logout_button);
        TextView username=root.findViewById(R.id.UsernameDisplay);
        TextView fullname=root.findViewById(R.id.fullnameDisplay);
        TextView email=root.findViewById(R.id.emailDisplay);
        TextView phoneNumber=root.findViewById(R.id.phDisplay);
        TextView birthday=root.findViewById(R.id.birthdayDisplay);
        TextView age=root.findViewById(R.id.ageDisplay);
        TextView country=root.findViewById(R.id.countryDisplay);
        TextView state=root.findViewById(R.id.stateDisplay);
        TextView occupation=root.findViewById(R.id.occpDisplay);
        TextView gender=root.findViewById(R.id.genderDisplay);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        String uid=auth.getCurrentUser().getUid();

        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue(String.class));
                fullname.setText(snapshot.child("fullName").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phoneNumber.setText(snapshot.child("phone_number").getValue(String.class));
                birthday.setText(snapshot.child("birthday").getValue(String.class));
                age.setText(snapshot.child("age").getValue(String.class));
                country.setText(snapshot.child("countryName").getValue(String.class));
                state.setText(snapshot.child("stateName").getValue(String.class));
                occupation.setText(snapshot.child("occupation").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditAccount_Activity.class);
                startActivity(intent);
            }
        });
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Starting_Activity.class);
                startActivity(intent);
            }
        });




        return root;
    }

}