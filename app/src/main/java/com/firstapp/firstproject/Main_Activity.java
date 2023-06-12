package com.firstapp.firstproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;

import com.firstapp.firstproject.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Main_Activity extends AppCompatActivity {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Toolbar mToolbar;
    ActivityMainBinding binding;

//    @SuppressLint("NonConstantResourceId")
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id==R.id.home) {
                replaceFragment(new HomeFragment());}
            else if (id == R.id.search_tab) {
                replaceFragment(new SearchForFriends_Fragment());
            }else if (id == R.id.request_tab) {
                replaceFragment(new Request_tab_Fragment());
            }else if (id == R.id.friends_tab) {
                replaceFragment(new Friends_list_Fragment());
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.appbar_dropdownlist_menu, menu);
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//        if (item.getItemId() == R.id.main_search_user_options){
//            sendUserToFindFriendsActivity();
//
//        }
//        return true;
//    }
//
//    private void sendUserToFindFriendsActivity(){
//        Intent findFriendIntent = new Intent(Main_Activity.this, FindFriendsActivity.class);
//        startActivity(findFriendIntent);
//    }
}