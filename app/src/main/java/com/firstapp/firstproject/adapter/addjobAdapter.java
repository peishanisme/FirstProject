package com.firstapp.firstproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;

import java.util.Stack;

public class addjobAdapter extends RecyclerView.Adapter<addjobAdapter.JobViewHolder> {
    private Stack<String> jobStack;


    public addjobAdapter(Stack<String> jobStack) {
        this.jobStack = jobStack;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_display, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        String job = jobStack.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobStack.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private TextView jobTextView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTextView = itemView.findViewById(R.id.jobTextView);
        }

        public void bind(String job) {
            jobTextView.setText(job);
        }
    }
}
