//package com.firstapp.firstproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class FindFriendsActivity extends AppCompatActivity {
//
//    private Toolbar mToolbar;
//    private ImageButton SearchButton;
//    private EditText SearchInputText;
//    private RecyclerView SearchResultList;
//    private DatabaseReference allUsersDatabaseRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_friends);
//
//        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        mToolbar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Find Friends");
//
//        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
//        SearchResultList.setHasFixedSize(true);
//        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
//
//        SearchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
//        SearchInputText = (EditText) findViewById(R.id.search_box_input);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Friend_Fragment fragment = new Friend_Fragment();
//        fragmentTransaction.add(R.id.addFriend_Fragment, fragment); // R.id.container is the ID of the container view in your activity's layout
//        fragmentTransaction.commit();
//
//        SearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchBoxInput = SearchInputText.getText().toString();
//                SearchPeopleAndFriends(searchBoxInput);
//            }
//
//        });
//    }
//
//    private void SearchPeopleAndFriends(String searchBoxInput) {
//        Toast.makeText(this,"Searching....", Toast.LENGTH_LONG).show();
//
//        //Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("fullName")
//          //      .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");
//
//        List<Query> queries = new ArrayList<>();
//
//        // Create a query for each child property you want to search
//        Query fullNameQuery = allUsersDatabaseRef.orderByChild("fullName").equalTo(searchBoxInput);
//        Query usernameQuery = allUsersDatabaseRef.orderByChild("username").equalTo(searchBoxInput);
//        Query useridQuery = allUsersDatabaseRef.orderByChild("userid").equalTo(searchBoxInput);
//        Query emailQuery = allUsersDatabaseRef.orderByChild("email").equalTo(searchBoxInput);
//        Query phoneNumberQuery = allUsersDatabaseRef.orderByChild("phone_number").equalTo(searchBoxInput);
//
//        // Add the queries to the list
//        queries.add(fullNameQuery);
//        queries.add(usernameQuery);
//        queries.add(useridQuery);
//        queries.add(emailQuery);
//        queries.add(phoneNumberQuery);
//
//
//        /** Combine the queries using `QueryUtils.combineQueries()` method
//        Query combinedQuery = QueryUtils.combineQueries(queries);
//
//        // Create a new List to store the combined query results
//        List<User> combinedResults = new ArrayList<>();
//
//        // Perform the queries individually and merge the results
//        for (Query query : queries) {
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        User user = snapshot.getValue(User.class);
//                        combinedResults.add(user);
//                    }
//
//                    // Perform additional actions or handle the combined results here
//                    // For example, update the RecyclerView with the combinedResults
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle the error
//                }
//            });
//        }**/
//
//        FirebaseRecyclerOptions<FindFriends> options =
//                new FirebaseRecyclerOptions.Builder<FindFriends>()
//                        .setQuery((Query) queries, FindFriends.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
//                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(options) {
//            @NonNull
//            @Override
//            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                Friend_Fragment fragment = new Friend_Fragment();
//                View fragmentView = fragment.getView();
//
//                View view = null;
//                if (fragmentView != null) {
//                    view = LayoutInflater.from(fragmentView.getContext()).inflate(R.layout.fragment_friend_, (ViewGroup) fragmentView, false);
//                }
//                return new FindFriendsViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFriends model) {
//                holder.myName.setText(model.getFullName());
//                holder.myOccupation.setText(model.getOccupation());
//
//                final int itemPosition = position;
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String visit_user_id = getRef(itemPosition).getKey();
//                        Intent profileIntent = new Intent(FindFriendsActivity.this, PersonProfileActivity.class);
//                        profileIntent.putExtra("visit_user_id", visit_user_id );
//                        startActivity(profileIntent);
//                    }
//                });
//
//            }
//
//            };
//        };
//
//
//
//        /**FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
//                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(
//                        FindFriends.class,
//                        R.layout.all_users_display_layout,
//                        FindFriendsViewHolder.class,
//                        searchPeopleandFriendsQuery
//        ){
//            @NonNull
//            @Override
//            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFriends model) {
//
//            }
//
//            @Override
//            protected void populateViewHolder (FindFriendsViewHolder viewHolder, FindFriends model, int position){
//                viewHolder.setFullName(model.getFullName());
//                viewHolder.setOccupation(model.getOccupation());
//                //viewHolder.setProfilePicture(getApplicationContext(), model.getProfilePicture());
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String visit_user_id = getRef(position).getKey();
//                        Intent profileIntent = new Intent(FindFriendsActivity.this, PersonProfileActivity.class);
//                        profileIntent.putExtra("visit_user_id", visit_user_id );
//                        startActivity(profileIntent);
//                    }
//                });
//            }
//
//        };
//        SearchResultList.setAdapter(firebaseRecyclerAdapter);**/
//    }
//
//     /**class FindFriendsViewHolder extends RecyclerView.ViewHolder{
//        View mView;
//        TextView myName;
//        TextView myOccupation;
//
//        public FindFriendsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView = itemView;
//        }
//
//        //public void setProfilePicture(Context ctx, String profilePicture) {
//            //CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
//            //Picasso.with(ctx).load(profilePicture).placeholder(R.drawable.profile).into(myImage);
//
//
//        //}
//
//        public void setFullName(String fullName) {
//            myName = (TextView) mView.findViewById(R.id.all_user_profile_full_name);
//            myName.setText(fullName);
//
//        }
//
//        public void setOccupation(String occupation) {
//            myOccupation = (TextView) mView.findViewById(R.id.all_user_occupation);
//            myOccupation.setText(occupation);
//        }
//
//    }
