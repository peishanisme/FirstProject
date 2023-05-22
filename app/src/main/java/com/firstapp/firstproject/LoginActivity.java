package com.firstapp.firstproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private EditText UserEmail,UserPhoneNumber,UserPassword;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        UserEmail=(EditText) findViewById(R.id.input_email);
        UserPassword=(EditText) findViewById(R.id.input_password);
        LoginButton=(Button) findViewById(R.id.login_button);
        loadingBar=new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login();
            }
        });
    }
    private void Login(){
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password...",Toast.LENGTH_SHORT).show();
        }else{
            //loading bar message
            loadingBar.setTitle("Logging in...");
            loadingBar.setMessage("Please wait, we are logging your account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(LoginActivity.this,"You are logged in successfully",Toast.LENGTH_SHORT).show();

                        loadingBar.dismiss();
                        SendUserToMainActivity();
                    }
                    else {
                        String message =task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error occured: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    }
            });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }
}