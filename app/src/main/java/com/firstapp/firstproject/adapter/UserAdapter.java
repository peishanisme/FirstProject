package com.firstapp.firstproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private OnDeleteAccountClickListener deleteAccountClickListener;

    public UserAdapter(List<User> userList, OnDeleteAccountClickListener deleteAccountClickListener) {
        this.userList = userList;
        this.deleteAccountClickListener = deleteAccountClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.usernameTextView.setText(user.getUsername());
        holder.fullNameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getEmail());
        holder.phoneNumberTextView.setText(user.getPhone_number());

        holder.deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteAccountClickListener != null) {
                    deleteAccountClickListener.onDeleteAccountClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView fullNameTextView;
        TextView emailTextView;
        TextView phoneNumberTextView;
        TextView deleteAccountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_adminac);
            fullNameTextView = itemView.findViewById(R.id.fullname_display_adminac);
            emailTextView = itemView.findViewById(R.id.emailDisplay);
            phoneNumberTextView = itemView.findViewById(R.id.phDisplay);
            deleteAccountTextView = itemView.findViewById(R.id.delete_account);
        }
    }

    public interface OnDeleteAccountClickListener {
        void onDeleteAccountClick(User user);
    }
}
