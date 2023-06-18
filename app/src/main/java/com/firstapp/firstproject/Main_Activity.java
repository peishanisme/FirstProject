package com.firstapp.firstproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import com.firstapp.firstproject.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Main_Activity extends AppCompatActivity {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Toolbar mToolbar;
    ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Automatic replace fragment to the respective fragment
        // based on the targetFragment when it redirect from the traceback function
        String targetFragment = getIntent().getStringExtra("targetFragment");

        if (targetFragment != null) {
            if (targetFragment.equals("Search Friends")) {
                targetFragment = null;
                replaceFragment(new SearchForFriends_Fragment());
            } else if (targetFragment.equals("Request")) {
                targetFragment = null;
                replaceFragment(new RequestFragment());
            } else if (targetFragment.equals("Profile")) {
                targetFragment = null;
                replaceFragment(new ProfileFragment());
            } else if (targetFragment.equals("Friend list")) {
                targetFragment = null;
                replaceFragment(new FriendList_Fragment());
            }
        } else {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id==R.id.home) {
                replaceFragment(new HomeFragment());}
            else if (id == R.id.search_tab) {
                replaceFragment(new SearchForFriends_Fragment());
            }else if (id == R.id.request_tab) {
                replaceFragment(new RequestFragment());
            }else if (id == R.id.friends_tab) {
                replaceFragment(new FriendList_Fragment());
            }else if(id==R.id.profile){
                replaceFragment(new ProfileFragment());
            }


            return true;
        });


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

}
