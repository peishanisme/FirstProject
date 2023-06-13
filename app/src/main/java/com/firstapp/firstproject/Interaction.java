package com.firstapp.firstproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Interaction extends AppCompatActivity {

    private RecyclerView interactList;

    private TextView timeTextView;
    private TextView interactionDetails, interactionContent, interactionTimeInfo;
    private Button interactionButton;

    static String fragment;

    //protected InteractionTracker<String> tracker = new InteractionTracker<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_interaction);


        interactList = (RecyclerView) findViewById(R.id.Interaction_Lists);
        //interactList.setHasFixedSize(true);
        interactList.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(InteractionTracker.interactionList, InteractionTracker.interactionLinkList, InteractionTracker.interactionTimeList);
        interactList.setAdapter(adapter);

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.InteractionViewHolder> {
        private LinkedList<String> mDataList1;
        private LinkedList<String> mDataList2;
        private LinkedList<String> mDataList3;

        public MyAdapter(LinkedList<String> dataList1, LinkedList<String> dataList2, LinkedList<String> dataList3) {
            this.mDataList1 = dataList1;
            this.mDataList2 = dataList2;
            this.mDataList3 = dataList3;
        }

        @NonNull
        @Override
        public InteractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_interaction, parent, false);
            return new InteractionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull InteractionViewHolder holder, int position) {
            String data1 = mDataList1.get(position);
            String data2 = mDataList2.get(position);
            String data3 = mDataList3.get(position);

            holder.setName(data1);
            holder.setContent(data2);
            holder.setTime(data3);
            interactionButton.setId(position); // Set a unique ID for each button
            interactionButton.setTag(InteractionTracker.interactionList.get(position)); // Set the corresponding interaction content as a tag


            interactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String interactionContent = (String) view.getTag();
                    // Handle the button click based on the interaction content
                    handleButtonClick(interactionContent);
                }
            });

        }

        @Override
        public int getItemCount() {
            // Return the total number of items in any of the LinkedLists
            return Math.min(Math.min(mDataList1.size(), mDataList2.size()), mDataList3.size());
        }

        public class InteractionViewHolder extends RecyclerView.ViewHolder {


            public InteractionViewHolder(View itemView) {
                super(itemView);
                interactionDetails = itemView.findViewById(R.id.Section_details);
                interactionContent = itemView.findViewById(R.id.Interaction_Content);
                interactionTimeInfo = itemView.findViewById(R.id.Time_info);
                interactionButton = itemView.findViewById(R.id.Go_Back_button);
            }

            public void setName(String name) {
                interactionDetails.setText(name);
            }

            public void setContent(String content) {
                interactionContent.setText(content);
            }

            public void setTime(String time) {
                interactionTimeInfo.setText(time);
            }

        }
    }


    private void handleButtonClick(String interactionContent) {
        fragment = null;
        // Determine which activity class to start based on the interaction content
        switch (interactionContent) {
            case "Post":
                // Start the Post_Fragment activity
                Intent postFragmentIntent = new Intent(Interaction.this, Post_Fragment.class);
                startActivity(postFragmentIntent);
                break;
            case "Home":
                // Start the Main_Activity
                Intent MainIntent = new Intent(Interaction.this, Main_Activity.class);
                MainIntent.putExtra("targetFragment", "Home");
                startActivity(MainIntent);
                break;
            case "Profile":
                // Start the Profile_Activity
                MainIntent = new Intent(Interaction.this, Main_Activity.class);
                MainIntent.putExtra("targetFragment", "Profile");
                startActivity(MainIntent);
                break;
            case "Friends":
                Intent FriendsActivityIntent = new Intent(Interaction.this, Friends.class);
                startActivity(FriendsActivityIntent);
                break;
            case "Add Friends":
                MainIntent = new Intent(Interaction.this, Main_Activity.class);
                MainIntent.putExtra("targetFragment", "Add Friends");
                startActivity(MainIntent);
                break;
            case"Edit Account":
                Intent EditAccIntent = new Intent(Interaction.this,EditAccount_Activity.class);
                startActivity(EditAccIntent);


            default:
                // Handle the default case if needed
                break;
        }

    }


}