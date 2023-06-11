package com.firstapp.firstproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;

import java.util.ArrayList;

public class addhobbyAdapter extends RecyclerView.Adapter<addhobbyAdapter.ViewHolder> {
    private ArrayList<String> hobbies;

    public addhobbyAdapter(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hobby_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String hobby = hobbies.get(position);
        holder.hobbyTextView.setText(hobby);
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobbyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hobbyTextView = (TextView)itemView.findViewById(R.id.hobby1);
        }
    }
}






