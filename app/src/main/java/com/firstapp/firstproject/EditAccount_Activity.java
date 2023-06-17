package com.firstapp.firstproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EditAccount_Activity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LinearLayout layoutList1;
    LinearLayout layoutList2;
    ImageView back_button;
    Button addJob;
    Button addHobby;
    String Username, fullname, phoneNumber, birthday, age, country, state, occupation, Gender, relationship;
    EditText EditUsername, EditFullName, EditPhoneN, EditBirthday, EditCountry, EditState, EditOccupation, EditRelationship;
    RadioGroup gender;
    RadioButton genderSelection;

    //Use Array List to store hobbies
    ArrayList<String> hobbyList = new ArrayList<>();

    //Use stack to store jobs
    Stack<String> jobStack = new Stack<>();
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);


        back_button=findViewById(R.id.backbutton);
        InteractionTracker.add("Edit Account");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        String uid = mAuth.getCurrentUser().getUid();

        EditUsername = findViewById(R.id.editUsername);
        EditFullName = findViewById(R.id.editFullName);
        EditPhoneN = findViewById(R.id.editPhoneN);
        EditBirthday = findViewById(R.id.editBirthday);
        EditCountry = findViewById(R.id.editCountry);
        EditState = findViewById(R.id.editState);
        EditOccupation = findViewById(R.id.editOccupation);
        EditRelationship = findViewById(R.id.editRelationship);
        gender = (RadioGroup) findViewById(R.id.editGender);
        save = findViewById(R.id.save_info);

        //dynamic view for addHobby
        layoutList1 = findViewById(R.id.hobby_layout);
        addHobby = findViewById(R.id.AddHobby);
        addHobby.setOnClickListener(this);

        //dynamic view for addJob
        layoutList2 = findViewById(R.id.job_layout);
        addJob = findViewById(R.id.AddJob);
        addJob.setOnClickListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //if statement to check whether the field is empty
                if (!EditUsername.getText().toString().isEmpty() && !EditFullName.getText().toString().isEmpty() && !EditPhoneN.getText().toString().isEmpty() && !EditBirthday.getText().toString().isEmpty() && !EditCountry.getText().toString().isEmpty() && !EditState.getText().toString().isEmpty() && !EditOccupation.getText().toString().isEmpty()) {
                   //if statement to check whether the data is changed
                    if (isUsernameChange() || isFullNameChange() || isPhoneNChange() || isBirthdayChange() || isCountryChange() || isStateChange() || isOccupationChange() || isRelationshipChange()) {
                        genderSelection = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
                        reference.child(uid).child("username").setValue(EditUsername.getText().toString());
                        reference.child(uid).child("fullName").setValue(EditFullName.getText().toString());
                        reference.child(uid).child("phone_number").setValue(EditPhoneN.getText().toString());
                        reference.child(uid).child("birthday").setValue(EditBirthday.getText().toString());
                        reference.child(uid).child("countryName").setValue(EditCountry.getText().toString());
                        reference.child(uid).child("stateName").setValue(EditState.getText().toString());
                        reference.child(uid).child("occupation").setValue(EditOccupation.getText().toString());
                        reference.child(uid).child("relationship").setValue(EditRelationship.getText().toString());
                        String EditAge = Setup_Activity.ageCalculation(EditBirthday.getText().toString());
                        reference.child(uid).child("age").setValue(EditAge);

                        //Retrieve input from addHobby dynamic view and add into ArrayList. Pass the array list into database for storing
                        hobbyList.clear();
                        for (int i =1;i<layoutList1.getChildCount();i++){
                            EditText ETHobby = (EditText) layoutList1.getChildAt(i).findViewById(R.id.newHobby);
                            if(ETHobby.getText().toString()!=null){
                                hobbyList.add(ETHobby.getText().toString());
                            }
                        }
                        DatabaseReference hobbyReference = FirebaseDatabase.getInstance().getReference("Hobbies");
                        hobbyReference.child(uid).setValue(hobbyList);

                        //Retrieve input from addJob dynamic view and push into Stack. Pass the stack into database for storing
                        jobStack.clear();
                        DatabaseReference jobReference = FirebaseDatabase.getInstance().getReference("Jobs");
                        jobReference.child(uid).setValue(jobStack);
                        for (int i =1;i<layoutList2.getChildCount();i++){
                            EditText ETJob = (EditText) layoutList2.getChildAt(i).findViewById(R.id.newJob);
                            if(ETJob.getText().toString()!=null){
                                jobStack.push(ETJob.getText().toString());
                            }
                        }
                        DatabaseReference job_Reference = FirebaseDatabase.getInstance().getReference("Jobs");
                        job_Reference.child(uid).setValue(jobStack);

                        Toast.makeText(EditAccount_Activity.this, "Saved", Toast.LENGTH_SHORT).show();
                        SendUserToProfileFragment();
                    } else {
                        Toast.makeText(EditAccount_Activity.this, "Please do not leave empty space", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //retrieve original data from "Users" node
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Username = snapshot.child("username").getValue(String.class);
                fullname = snapshot.child("fullName").getValue(String.class);
                phoneNumber = snapshot.child("phone_number").getValue(String.class);
                birthday = snapshot.child("birthday").getValue(String.class);
                age = snapshot.child("age").getValue(String.class);
                country = snapshot.child("countryName").getValue(String.class);
                state = snapshot.child("stateName").getValue(String.class);
                occupation = snapshot.child("occupation").getValue(String.class);
                Gender = snapshot.child("gender").getValue(String.class);
                relationship = snapshot.child("relationship").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //method to control dynamic view
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.AddJob) {
            addJobView();
        } else if (v.getId() == R.id.AddHobby) {
            addHobbyView();
        }
    }

    private void addJobView() {
        final View jobView = getLayoutInflater().inflate(R.layout.dynamic_view_job, null, false);
        final EditText jobEdit = (EditText) jobView.findViewById(R.id.newJob);
        ImageView removeJob = (ImageView) jobView.findViewById(R.id.button_remove);

        removeJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeJobView(jobView);
            }
        });

        layoutList2.addView(jobView);
        jobStack.push(jobEdit.getText().toString());
    }

    private void addHobbyView() {
        final View hobbyView = getLayoutInflater().inflate(R.layout.dynamic_view_hobby, null, false);
        final EditText hobbyEdit = (EditText) hobbyView.findViewById(R.id.newHobby);
        ImageView removeHobby = (ImageView) hobbyView.findViewById(R.id.image_remove);

        removeHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeHobbyView(hobbyView);
            }
        });

        layoutList1.addView(hobbyView);
        hobbyList.add(hobbyEdit.getText().toString());
    }

    public void removeJobView(View v) {
        layoutList2.removeView(v);
    }

    public void removeHobbyView(View v) {
        layoutList1.removeView(v);
    }


    //methods to check whether the data is changed
    public boolean isUsernameChange() {
        if (!Username.equals(EditUsername.getText().toString())) {
            Username = EditUsername.getText().toString();
            return true;
        } else if (Username == null) {
            Username = EditUsername.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isFullNameChange() {
        if (!fullname.equals(EditFullName.getText().toString())) {
            fullname = EditFullName.getText().toString();
            return true;
        } else if (fullname == null) {
            fullname = EditFullName.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhoneNChange() {
        if (!phoneNumber.equals(EditPhoneN.getText().toString())) {
            phoneNumber = EditPhoneN.getText().toString();
            return true;
        } else if (phoneNumber == null) {
            phoneNumber = EditPhoneN.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isBirthdayChange() {
        if (!birthday.equals(EditBirthday.getText().toString())) {
            birthday = EditBirthday.getText().toString();
            return true;
        } else if (birthday == null) {
            birthday = EditBirthday.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isCountryChange() {
        if (!country.equals(EditCountry.getText().toString())) {
            country = EditCountry.getText().toString();
            return true;
        } else if (country == null) {
            country = EditCountry.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isStateChange() {
        if (!state.equals(EditState.getText().toString())) {
            state = EditState.getText().toString();
            return true;
        } else if (state == null) {
            state = EditState.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isOccupationChange() {
        if (!occupation.equals(EditOccupation.getText().toString())) {
            occupation = EditOccupation.getText().toString();
            return true;
        } else if (occupation == null) {
            occupation = EditOccupation.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isRelationshipChange() {
        if (!relationship.equals(EditRelationship.getText().toString())) {
            relationship = EditRelationship.getText().toString();
            return true;
        } else if (relationship == null) {
            relationship = EditRelationship.getText().toString();
            return true;
        } else {
            return false;
        }
    }


//Method to send user to profile fragment
    private void SendUserToProfileFragment() {
        Intent intent = new Intent(EditAccount_Activity.this, ProfileFragment.class);
        startActivity(intent);

    }
}
