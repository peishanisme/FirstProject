package com.firstapp.firstproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.ViewAccActivity_Scrollview;
import com.firstapp.firstproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.UserViewHolder> {
    private static List<User> userList;
    private Context context;

    public SearchFriendAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_display, parent, false);
        context= parent.getContext();
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.usernameTextView.setText(user.getUsername());
        holder.fullnameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getEmail());
        holder.phonenumberTextView.setText(user.getPhone_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAccActivity_Scrollview.class);
                intent.putExtra("uid", user.getUid()); // Pass the user object
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView fullnameTextView;
        TextView emailTextView;
        TextView phonenumberTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.all_user_profile_username);
            fullnameTextView = itemView.findViewById(R.id.all_user_fullname);
            emailTextView = itemView.findViewById(R.id.all_user_email);
            phonenumberTextView = itemView.findViewById(R.id.all_user_phonenumber);
        }

    }
}





