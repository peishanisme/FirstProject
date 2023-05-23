package com.firstapp.firstproject;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import com.firstapp.firstproject.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

//    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

//            switch(item.getItemId()){
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.new_post:
//                    break;
//                case R.id.profile:
//                    replaceFragment(new ProfileFragment());
//                    break;
//            }
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.new_post) {
                // Handle another case
            } else if(id==R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }
}