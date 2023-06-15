package com.firstapp.firstproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.firstapp.firstproject.ViewAccActivity_Scrollview;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.FriendRequestViewHolder> {

    private List<User> requestList;
    private Context context;
    private ViewAccActivity_Scrollview view = new ViewAccActivity_Scrollview();

    public RequestAdapter(List<User> requestList, Context context) {
        this.requestList=requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_display, parent, false);
        return new RequestAdapter.FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        User request = requestList.get(position);
        holder.username.setText(request.getUsername());
        holder.fullname.setText(request.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAccActivity_Scrollview.class);
                intent.putExtra("uid", request.getUid()); // Pass the user object
                context.startActivity(intent);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(request.userid);
                FirebaseDatabase.getInstance().getReference("Friends").child(request.userid).child(FirebaseAuth.getInstance().getUid());
                FirebaseDatabase.getInstance().getReference("FriendRequests").child(FirebaseAuth.getInstance().getUid()).child(request.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference("FriendRequests").child(request.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("FriendRequests").child(FirebaseAuth.getInstance().getUid()).child(request.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference("FriendRequests").child(request.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue();
            }
        });



    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView fullname;

        private Button accept;
        private Button delete;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.friend_username);
            fullname=itemView.findViewById(R.id.friend_fullname_display);
            accept = itemView.findViewById(R.id.accapt_button);
            delete = itemView.findViewById(R.id.delete_button);
        }
    }
}

//            acceptButton.setOnClickListener(v -> {
//                String receiverUserId = friendRequest.getUid();
//                friendsRef.child(currentUserId).child(receiverUserId).setValue(true)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                friendsRef.child(receiverUserId).child(currentUserId).setValue(true)
//                                        .addOnCompleteListener(task1 -> {
//                                            if (task1.isSuccessful()) {
//                                                friendRequestsRef.child(currentUserId).child(receiverUserId).removeValue()
//                                                        .addOnCompleteListener(task2 -> {
//                                                            if (task2.isSuccessful()) {
//                                                                friendRequestsRef.child(receiverUserId).child(currentUserId).removeValue()
//                                                                        .addOnCompleteListener(task3 -> {
//                                                                            if (task3.isSuccessful()) {
//                                                                                // Request completed successfully
//                                                                            }
//                                                                        });
//                                                            }
//                                                        });
//                                            }
//                                        });
//                            }
//                        });
//            });
//
//            deleteButton.setOnClickListener(v -> {
//                String receiverUserId = friendRequest.getUid();
//                friendRequestsRef.child(currentUserId).child(receiverUserId).removeValue()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                friendRequestsRef.child(receiverUserId).child(currentUserId).removeValue()
//                                        .addOnCompleteListener(task1 -> {
//                                            if (task1.isSuccessful()) {
//                                                // Request deleted successfully
//                                            }
//                                        });
//                            }
//                        });
//            });
//        }


//package com.firstapp.firstproject.adapter;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firstapp.firstproject.R;
//import com.firstapp.firstproject.entity.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.FriendRequestViewHolder> {
//
//    private List<User> friendRequestList;
//    private Context context;
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private DatabaseReference friendRequestsRef;
//    private DatabaseReference friendsRef;
//    String currentUserId;
//
//    public RequestAdapter(List<User> friendRequestList, Context context) {
//        this.friendRequestList = friendRequestList;
//        this.context = context;
//        friendRequestsRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
//        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
//        currentUserId = mAuth.getCurrentUser().getUid();
//
//
//    }
//
//    @NonNull
//    @Override
//    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.request_display, parent, false);
//        return new FriendRequestViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
//        User friendRequest = friendRequestList.get(position);
//        holder.bind(friendRequest);
//    }
//
//    @Override
//    public int getItemCount() {
//        return friendRequestList.size();
//    }
//
//    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
//        private TextView username;
//        private TextView fullname;
//        private Button acceptButton;
//        private Button deleteButton;
//
//        public FriendRequestViewHolder(@NonNull View itemView) {
//            super(itemView);
//            username = itemView.findViewById(R.id.friend_username);
//            fullname = itemView.findViewById(R.id.friend_fullname_display);
//            acceptButton = itemView.findViewById(R.id.accapt_button);
//            deleteButton = itemView.findViewById(R.id.delete_button);
//        }
//
//        public void bind(User friendRequest) {
//            username.setText(friendRequest.getUsername());
//            fullname.setText(friendRequest.getFullName());
//
//
//            acceptButton.setOnClickListener(v -> {
//                        String receiverUserId = friendRequest.getUid();
//
////                // Delete friend request from FriendRequests node
////                friendRequestsRef.child(receiverUserId).child(currentUserId).removeValue();
////                friendRequestsRef.child(currentUserId).child(receiverUserId).removeValue();
////
////                // Add users as friends in the Friends node
////                friendsRef.child(currentUserId).child(receiverUserId).setValue(true);
////                friendsRef.child(receiverUserId).child(currentUserId).setValue(true);
//
//                        friendsRef.child(currentUserId).child(receiverUserId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    friendsRef.child(receiverUserId).child(currentUserId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                friendRequestsRef.child(currentUserId).child(receiverUserId).removeValue()
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    friendRequestsRef.child(receiverUserId).child(currentUserId).removeValue()
//                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                                @Override
//                                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                                    if (task.isSuccessful()) {
//
//                                                                                    }
//                                                                                }
//                                                                            });
//                                                                }
//                                                            }
//                                                        });
//                                            }
//
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    });
//
//
//
//
//            deleteButton.setOnClickListener(v -> {
//                String receiverUserId = friendRequest.getUid();
//
////                // Delete friend request from FriendRequests node
////                friendRequestsRef.child(receiverUserId).child(currentUserId).removeValue();
////                friendRequestsRef.child(currentUserId).child(receiverUserId).removeValue();
//
//                friendRequestsRef.child(currentUserId).child(receiverUserId)
//                        .removeValue()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    friendRequestsRef.child(receiverUserId).child(currentUserId)
//                                            .removeValue()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful()){
//
//                                                    }
//                                                }
//                                            });
//                                }
//                            }
//                        });
//
//
//            });
//        }
//    }
//}
