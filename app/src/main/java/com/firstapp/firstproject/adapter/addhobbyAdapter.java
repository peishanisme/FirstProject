package com.firstapp.firstproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.firstproject.R;
import com.firstapp.firstproject.entity.Hobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class addhobbyAdapter extends RecyclerView.Adapter<addhobbyAdapter.AddHobbyView>{

    ArrayList<String> hobbyArrayList = new ArrayList<>();

    public addhobbyAdapter(ArrayList<String> hobbyArrayList) {
        this.hobbyArrayList = hobbyArrayList;
    }

    @NonNull
    @Override
    public AddHobbyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_view_hobby,parent,false);
        return new AddHobbyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addhobbyAdapter.AddHobbyView holder, int position) {
        hobbyArrayList.add(holder.hobby_ET.getText().toString());
    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CricketerView holder, int position) {
//
//        Cricketer cricketer = cricketersList.get(position);
//        holder.textCricketerName.setText(cricketer.getCricketerName());
//        holder.textTeamName.setText(cricketer.getTeamName());
//
//
//    }

    @Override
    public int getItemCount() {
        return hobbyArrayList.size();
    }

    public class AddHobbyView extends RecyclerView.ViewHolder{
        EditText hobby_ET;
        public AddHobbyView(@NonNull View itemView) {
            super(itemView);
            hobby_ET = itemView.findViewById(R.id.newHobby);
        }
    }
}


