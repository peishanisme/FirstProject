package com.firstapp.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View root;
    private ImageButton AddNewPostButton;
    private ImageButton HistoryButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AddNewPostButton = view.findViewById(R.id.addpostButton); 
        HistoryButton = view.findViewById(R.id.historyButton) ;
        InteractionTracker.add("Home","null"); // if the user enter this fragment, it will record the title as Home and no interaction link inside it.
        
        HistoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Interaction.class);
                startActivity(intent);
            }
        });

        AddNewPostButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Post_Fragment.class);
                startActivity(intent);
            }
        });

        
        // Set up the views and any other necessary logic for the fragment
        return view;
    }
}
