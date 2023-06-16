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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.FriendRequestViewHolder> {

    private List<User> requestList;
    private Context context;
    private String saveCurrentDate;

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
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
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
                FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(request.userid).child("date").setValue(saveCurrentDate);
                FirebaseDatabase.getInstance().getReference("Friends").child(request.userid).child(FirebaseAuth.getInstance().getUid()).child("date").setValue(saveCurrentDate);
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

