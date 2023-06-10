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
//import android.widget.Toolbar;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.firstapp.firstproject.entity.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchForFriends_Fragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private Toolbar mToolbar;
//    private ImageButton SearchButton;
//    private EditText SearchInputText;
//    private RecyclerView FindFriendsRecyclerList;
//    private View SeacrhView;
//
//    private DatabaseReference UsersRef;
//    public SearchForFriends_Fragment() {
//        // Required empty public constructor
//    }
//
//
//    public static SearchForFriends_Fragment newInstance(String param1, String param2) {
//        SearchForFriends_Fragment fragment = new SearchForFriends_Fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        SeacrhView = inflater.inflate(R.layout.fragment_seacrh_for_friends_, container, false);
//        FindFriendsRecyclerList = (RecyclerView) SeacrhView.findViewById(R.id.search_result_list);
//        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        SearchButton = (ImageButton) SeacrhView.findViewById(R.id.search_people_friends_button);
//        SearchInputText = (EditText) SeacrhView.findViewById(R.id.search_box_input);
//
////        /**mToolbar = SeacrhView.findViewById(R.id.find_friends_appbar_layout);
////        AppCompatActivity activity = (AppCompatActivity) getActivity();
////        activity.setSupportActionBar(mToolbar);
////        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
////        activity.getSupportActionBar().setTitle("Find Friends");**/
//
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        SearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String searchBoxInput = SearchInputText.getText().toString();
////                SearchPeopleAndFriends(searchBoxInput);
//            }
//
//        });
//
//        return SeacrhView;
//
//    }
//    //List<Query> queries = new ArrayList<>();
//    Query query;
//    private void SearchPeopleAndFriends(String searchBoxInput) {
//        //Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show();
//
//        query = UsersRef.orderByChild("fullName")
//              .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");
//
//        //List<Query> queries = new ArrayList<>();
//
//        // Create a query for each child property you want to search
////        Query fullNameQuery = UsersRef.orderByChild("fullName").equalTo(searchBoxInput);
////        Query usernameQuery = UsersRef.orderByChild("username").equalTo(searchBoxInput);
////        Query useridQuery = UsersRef.orderByChild("userid").equalTo(searchBoxInput);
////        Query emailQuery = UsersRef.orderByChild("email").equalTo(searchBoxInput);
////        Query phoneNumberQuery = UsersRef.orderByChild("phone_number").equalTo(searchBoxInput);
//
//        // Add the queries to the list
////        queries.add(fullNameQuery);
////        queries.add(usernameQuery);
////        queries.add(useridQuery);
////        queries.add(emailQuery);
////        queries.add(phoneNumberQuery);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        String searchBoxInput = SearchInputText.getText().toString();
//        SearchPeopleAndFriends(searchBoxInput);
//
//
//        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
//                .setQuery(query, User.class).build();
//
//        FirebaseRecyclerAdapter<User, FindFriendViewHolder> adapter =
//                new FirebaseRecyclerAdapter<User, FindFriendViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull User model) {
//                        holder.userName.setText(model.getFullName());
//                        holder.userOccupation.setText(model.getOccupation());
//                        final int itemPosition = position;
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String visit_user_id = getRef(itemPosition).getKey();
//                                Intent profileIntent = new Intent(getActivity(), ViewAccActivity_Scrollview.class);
//                                //Intent profileIntent = new Intent(SeacrhForFriends_Fragment.this, ViewAccActivity_Scrollview.class);
//                                profileIntent.putExtra("visit_user_id", visit_user_id);
//                                startActivity(profileIntent);
//                            }
//                        });
//                    }
//
//
//                    @NonNull
//                    @Override
//                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_acc_scrollview,parent,false);
//                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//        FindFriendsRecyclerList.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{
//
//        TextView userName, userOccupation;
//        View mView;
//        public FindFriendViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView = itemView;
//
//            //public void setFullName(String fullName) {
//            // userName = (TextView) mView.findViewById(R.id.all_user_profile_full_name);
//            //userName.setText(fullName);
//
//            //}
//
//            //public void setOccupation(String occupation) {
//            //    userOccupation = (TextView) mView.findViewById(R.id.all_user_occupation);
//            //    userOccupation.setText(occupation);
//            // }
//
//            userName = itemView.findViewById(R.id.all_user_profile_full_name);
//            userOccupation = itemView.findViewById(R.id.all_user_occupation);
//
//        }
//    }
//}