package com.firstapp.firstproject;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
//    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
private EditText UserName,UserEmail,UserPhoneNumber,UserPassword,UserConfrimPassword;
private ProgressDialog loadingBar;
private Button CreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();

        UserName=(EditText) findViewById(R.id.register_username);
        UserEmail=(EditText) findViewById(R.id.register_email);
        UserPhoneNumber=(EditText) findViewById(R.id.register_phonenumber) ;
        UserPassword=(EditText) findViewById(R.id.register_password);
        UserConfrimPassword=(EditText) findViewById(R.id.register_cofirmpassword);
        CreateAccountButton=(Button) findViewById(R.id.register_create_account);

        loadingBar=new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //method of creating new account
                CreateNewAccount();
            }
        });

    }
    public void CreateNewAccount(){

        //get text from user input
        String username=UserName.getText().toString();
        String email=UserEmail.getText().toString();
        String phoneNumber=UserPhoneNumber.getText().toString();
        String password=UserPassword.getText().toString();
        String confirmPassword=UserPassword.getText().toString();

        //prompt user to key in the information if the column is still empty
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please insert your username...",Toast.LENGTH_SHORT);

        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please insert your email...",Toast.LENGTH_SHORT);

        }else if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Please insert your phone number...",Toast.LENGTH_SHORT);

        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please insert your password...",Toast.LENGTH_SHORT);

        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please insert your password...",Toast.LENGTH_SHORT);

        }else if(!(password.equals(confirmPassword))){
            Toast.makeText(this,"Your password do not match with your confirm password...",Toast.LENGTH_SHORT);

        }else{
            //all the columns are filled,start creating new account

            //loading bar message
            loadingBar.setTitle("Creating New Account...");
            loadingBar.setMessage("Please wait, we are creating your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            //use email and password to create
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //tell user whether they successful to create account or not
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"You are authenticated successfully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else {
                        //display the error message to users
                        String message=task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this,"Error Occured"+message,Toast.LENGTH_SHORT);
                        loadingBar.dismiss();
                    }
                }
            });
        }

    }
}