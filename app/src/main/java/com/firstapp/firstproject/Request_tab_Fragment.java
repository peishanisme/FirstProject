package com.firstapp.firstproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Request_tab_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Request_tab_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View RequestFragmentView;

    private RecyclerView myRequestList;

    private DatabaseReference RequestRef, UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    public Request_tab_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Request_tab_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Request_tab_Fragment newInstance(String param1, String param2) {
        Request_tab_Fragment fragment = new Request_tab_Fragment();
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
        RequestFragmentView= inflater.inflate(R.layout.fragment_request_tab_, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");

        myRequestList = (RecyclerView) RequestFragmentView.findViewById(R.id.rv_request_list);
        myRequestList.setLayoutManager(new LinearLayoutManager(getContext()));
        return RequestFragmentView;
    }

    public void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(RequestRef.child(currentUserID), User.class).build();

        FirebaseRecyclerAdapter<User, RequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull User model) {
                        holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.request_cancel_button).setVisibility(View.VISIBLE);

                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("CURRENT_STATE");

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();

                                    if(type.equals("request_received")){
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if(snapshot.hasChild("fullName")){
                                                    final String requestUserName = snapshot.child("fullName").getValue().toString();
                                                    final String requestUserOccupation = snapshot.child("occupation").getValue().toString();

                                                    holder.fullName.setText(requestUserName);
                                                    holder.occupation.setText(requestUserOccupation);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout,parent,false);
                        RequestViewHolder holder = new RequestViewHolder(view);
                        return holder;
                    }
                };

        myRequestList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, occupation;
        Button AcceptButton, CancelButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.all_user_profile_full_name);
            occupation = itemView.findViewById(R.id.all_user_occupation);
            AcceptButton = itemView.findViewById(R.id.request_accept_button);
            CancelButton = itemView.findViewById(R.id.request_cancel_button);
        }
    }


}