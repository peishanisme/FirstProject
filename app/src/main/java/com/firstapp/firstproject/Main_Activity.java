package com.firstapp.firstproject;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import com.firstapp.firstproject.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Main_Activity extends AppCompatActivity {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ActivityMainBinding binding;

//    @SuppressLint("NonConstantResourceId")
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
                replaceFragment(new HomeFragment());
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
}