package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAccount_Activity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LinearLayout layoutList1;
    LinearLayout layoutList2;
    Button addJob;
    Button addHobby;
    EditText EditUsername,EditFullName,EditPhoneN,EditBirthday,EditCountry,EditState,EditOccupation,EditRelationship;
    RadioGroup gender;
    RadioButton genderSelection;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);


        FirebaseDatabase storage= FirebaseDatabase.getInstance();
        DatabaseReference storageReference = storage.getReference();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        String uid=mAuth.getCurrentUser().getUid();

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Username=snapshot.child("username").getValue(String.class);
                String fullname=snapshot.child("fullName").getValue(String.class);
                String phoneNumber=snapshot.child("phone_number").getValue(String.class);
                String birthday=snapshot.child("birthday").getValue(String.class);
                String age=snapshot.child("age").getValue(String.class);
                String country=snapshot.child("countryName").getValue(String.class);
                String state=snapshot.child("stateName").getValue(String.class);
                String occupation=snapshot.child("occupation").getValue(String.class);
                String gender=snapshot.child("gender").getValue(String.class);
                String relationship=snapshot.child("relationship").getValue(String.class);
                //hobby & job

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        EditUsername=findViewById(R.id.editUsername);
        EditFullName=findViewById(R.id.editFullName);
        EditPhoneN=findViewById(R.id.editPhoneN);
        EditBirthday=findViewById(R.id.editBirthday);
        EditCountry=findViewById(R.id.editCountry);
        EditState=findViewById(R.id.editState);
        EditOccupation=findViewById(R.id.editOccupation);
        EditRelationship=findViewById(R.id.editRelationship);
        gender=(RadioGroup)findViewById(R.id.editGender);
        save=findViewById(R.id.save_info);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EditUsername.getText().toString().isEmpty() &&!EditFullName.getText().toString().isEmpty()&&!EditPhoneN.getText().toString().isEmpty()&&!EditBirthday.getText().toString().isEmpty()&&!EditCountry.getText().toString().isEmpty()&&!EditState.getText().toString().isEmpty()&&!EditOccupation.getText().toString().isEmpty()){

                }
            }
        });


        layoutList1 = findViewById(R.id.hobby_layout);
        addHobby = findViewById(R.id.AddHobby);
        addHobby.setOnClickListener(this);

        layoutList2 = findViewById(R.id.job_layout);
        addJob = findViewById(R.id.AddJob);
        addJob.setOnClickListener(this);

    }

    @Override

    public void onClick(View v) {
        if (v.getId() == R.id.AddJob) {
            addJobView();
        } else if (v.getId() == R.id.AddHobby) {
            addHobbyView();
        }
    }


    private void addJobView() {
        View jobView = getLayoutInflater().inflate(R.layout.dynamic_view_job, null, false);

        EditText editText = jobView.findViewById(R.id.newJob);
        ImageView imageClose = jobView.findViewById(R.id.button_remove);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeJobView(jobView);
            }
        });

        layoutList2.addView(jobView);
    }


    private void addHobbyView() {
        View hobbyView = getLayoutInflater().inflate(R.layout.dynamic_view_hobby, null, false);

        EditText editText = hobbyView.findViewById(R.id.newHobby);
        ImageView imageClose = hobbyView.findViewById(R.id.image_remove);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeHobbyView(hobbyView);
            }
        });

        layoutList1.addView(hobbyView);
    }

    public void removeJobView(View v) {

        layoutList2.removeView(v);
    }

    public void removeHobbyView(View v) {
        layoutList1.removeView(v);

    }
}