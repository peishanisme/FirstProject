package com.firstapp.firstproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Period;

import com.firstapp.firstproject.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup_Activity extends AppCompatActivity {
    private EditText FullName, CountryName, StateName, Birthday, Occupation;
    private RadioGroup gender;
    private RadioButton genderSelection;
    private Button SaveInfoButton;
    private CircleImageView ProfilePicture;
    Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    String currentUserID;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user=snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FullName = (EditText) findViewById(R.id.fullname_input);
        CountryName = (EditText) findViewById(R.id.country_input);
        StateName = (EditText) findViewById(R.id.state_input);
        Birthday = (EditText) findViewById(R.id.birthday_input);
        Occupation = (EditText) findViewById(R.id.occupation_input);
        gender = (RadioGroup) findViewById(R.id.gender_RG);
        SaveInfoButton = (Button) findViewById(R.id.save_info);
        ProfilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        loadingBar = new ProgressDialog(this);


        SaveInfoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            private void SaveAccountSetupInformation() {

                String fullName = FullName.getText().toString();
                String countryName = CountryName.getText().toString();
                String stateName = StateName.getText().toString();
                String birthday = Birthday.getText().toString();
                String occupation = Occupation.getText().toString();
                genderSelection = (RadioButton) findViewById(gender.getCheckedRadioButtonId());

                //calculate age based on birthday
                LocalDate today = LocalDate.now();
                LocalDate Birthday = LocalDate.of(Integer.parseInt(birthday.substring(0, 4)), Integer.parseInt(birthday.substring(5, 7)), Integer.parseInt(birthday.substring(8, 10))); //Birth date
                Period p = Period.between(Birthday, today);
                String age=String.format("%2d", p.getYears());


                if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(countryName) || TextUtils.isEmpty(stateName) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(occupation)) {
                    Toast.makeText(Setup_Activity.this, "Please insert your information...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Setting up account...");
                    loadingBar.setMessage("Please wait, we are saving your account information...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    user.setFullName(fullName);
                    user.setCountryName(countryName);
                    user.setStateName(stateName);
                    user.setBirthday(birthday);
                    user.setAge(age);
                    user.setOccupation(occupation);
                    user.setGender(genderSelection.getText().toString());

//                    uploadPicture();
                    reference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Setup_Activity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                    } else {
                                        Toast.makeText(Setup_Activity.this, "Something error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Setup_Activity.this, Main_Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}




