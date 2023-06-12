package com.firstapp.firstproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.firstapp.firstproject.adapter.SearchFriendAdapter;
import com.firstapp.firstproject.entity.FindFriends;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class SearchForFriends_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;
    private RecyclerView FindFriendsRecyclerList;
    private View SeacrhView;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    FirebaseRecyclerAdapter<FindFriends, FindFriendViewHolder> firebaseRecyclerAdapter;

    public SearchForFriends_Fragment() {
        // Required empty public constructor
    }


    public static SearchForFriends_Fragment newInstance(String param1, String param2) {
        SearchForFriends_Fragment fragment = new SearchForFriends_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SeacrhView = inflater.inflate(R.layout.fragment_seacrh_for_friends_, container, false);
        FindFriendsRecyclerList = (RecyclerView) SeacrhView.findViewById(R.id.search_result_list);
        FindFriendsRecyclerList.setHasFixedSize(true);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));

        SearchButton = (ImageButton) SeacrhView.findViewById(R.id.search_people_friends_button);
        SearchInputText = (EditText) SeacrhView.findViewById(R.id.search_box_input);

//        mToolbar = SeacrhView.findViewById(R.id.find_friends_appbar_layout);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(mToolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
//        activity.getSupportActionBar().setTitle("Find Friends");

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();



        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = SearchInputText.getText().toString();
                SearchPeopleAndFriends(searchBoxInput);
            }

        });

        return SeacrhView;

    }


    private void SearchPeopleAndFriends(String searchBoxInput) {
        Toast.makeText(getContext(), "Searching", Toast.LENGTH_LONG).show();

        //Query searchPeopleandFriendsQuery = UsersRef.orderByChild("fullName")
         //       .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        Query searchPeopleandFriendsQuery = UsersRef.orderByChild("fullName").equalTo(searchBoxInput);

        FirebaseRecyclerOptions<FindFriends> options = new FirebaseRecyclerOptions.Builder<FindFriends>()
                .setQuery(searchPeopleandFriendsQuery, FindFriends.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriends, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull FindFriends model) {
                holder.userName.setText(model.getFullName());
                holder.userOccupation.setText(model.getOccupation());

                holder.itemView.setOnClickListener(v -> {
                    String visit_user_id = getRef(position).getKey();
                    Intent profileIntent = new Intent(getActivity(), ViewAccActivity_Scrollview.class);
                    //Intent profileIntent = new Intent(SeacrhForFriends_Fragment.this, ViewAccActivity_Scrollview.class);
                    profileIntent.putExtra("visit_user_id", visit_user_id);
                    startActivity(profileIntent);
                });
            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout,parent,false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        FindFriendsRecyclerList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//    }

    public void onStop(){
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userOccupation;
        View mView;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setFullName(String fullName) {
            userName = (TextView) mView.findViewById(R.id.all_user_profile_full_name);
            userName.setText(fullName);

        }

        public void setOccupation(String occupation) {
            userOccupation = (TextView) mView.findViewById(R.id.all_user_occupation);
            userOccupation.setText(occupation);
         }

    }
}
