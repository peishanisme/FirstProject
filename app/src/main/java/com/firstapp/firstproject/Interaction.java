package com.firstapp.firstproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.LinkedList;

public class Interaction extends AppCompatActivity {

    private RecyclerView interactList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_interaction);

        interactList = findViewById(R.id.Interaction_Lists);
        interactList.setHasFixedSize(true); // set as fixed size, optimize the performance
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true); // make the latest interactionat the top
        interactList.setLayoutManager(linearLayoutManager); // set the layout of this page to the format of linearLayoutManager
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
            // viewholder for the part that will repeatly displayed like every single interaction will be displayed
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_interaction, parent, false);
            return new InteractionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull InteractionViewHolder holder, int position) {
            String data1 = mDataList1.get(position);
            String data2 = mDataList2.get(position);
            String data3 = mDataList3.get(position);

            holder.setName(data1); // set the name of fragment to data1, which will be the fragment name
            holder.setContent(data2); // set the name of content to data2, which will display the relevant data like uid of the user
            holder.setTime(data3); // set the local time to the time

            // Set click listener for the button
            holder.interactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleButtonClick(data1, data2); // when click on the button, the application will jump to the handleButtonClick method
                }
            });
        }

        @Override
        public int getItemCount() { // count each lists' size
            return Math.min(Math.min(mDataList1.size(), mDataList2.size()), mDataList3.size());
        }

        public class InteractionViewHolder extends RecyclerView.ViewHolder {
            private TextView interactionDetails;
            private TextView interactionContent;
            private TextView interactionTimeInfo;
            private Button interactionButton;

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

    private void handleButtonClick(String interactionContent, String id) {
            // Handle the button click based on the interaction content
        if(interactionContent.equals( "Post")){
            // Start the Post_Fragment activity
            Intent postFragmentIntent = new Intent(Interaction.this, Post_Fragment.class);
            startActivity(postFragmentIntent);

        }
        else if(interactionContent.equals( "Home")) {
            //Start the Main_Activity, then redirect to Home fragment
            Intent MainIntent = new Intent(Interaction.this, Main_Activity.class);
            MainIntent.putExtra("targetFragment", "Home");
            startActivity(MainIntent);
        }
        else if(interactionContent.equals( "Profile")) {
            //Start the Main_Activity, then redirect to Profile fragment
            Intent MainIntent = new Intent(Interaction.this, Main_Activity.class);
            MainIntent.putExtra("targetFragment", "Profile");
            startActivity(MainIntent);
        }
        else if(interactionContent.equals( "Friend list")) {
            //Start the Main_Activity, then redirect to Friend list fragment
            Intent FriendsActivityIntent = new Intent(Interaction.this, Main_Activity.class);
            FriendsActivityIntent.putExtra("targetFragment", "Friend list");
            startActivity(FriendsActivityIntent);
        }
        else if(interactionContent.equals( "Search Friends")) {
            //Start the Main_Activity, then redirect to search friends fragment
            Intent MainIntent = new Intent(Interaction.this, Main_Activity.class);
            MainIntent.putExtra("targetFragment", "Search Friends");
            startActivity(MainIntent);
        }
        else if(interactionContent.equals( "Edit Account")) {
            //Start the EditAccount_Activity
            Intent EditAccIntent = new Intent(Interaction.this, EditAccount_Activity.class);
            startActivity(EditAccIntent);
        }
        else if(interactionContent.equals( "Request")) {
            //Start the Main_Activity, then redirect to Request fragment
            Intent MainIntent = new Intent(Interaction.this, Main_Activity.class);
            MainIntent.putExtra("targetFragment", "Request");
            startActivity(MainIntent);
        }
        else if(interactionContent.equals( "View Account")) {
            //Start the Main_Activity, then redirect to ViewAccActivity_Scrollview class
            Intent ViewAccountIntent = new Intent(Interaction.this, ViewAccActivity_Scrollview.class);
            ViewAccountIntent.putExtra("uid", id);
            startActivity(ViewAccountIntent);
        }
        else ;
    }
}
