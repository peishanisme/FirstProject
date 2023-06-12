//package com.firstapp.firstproject.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firstapp.firstproject.R;
//import com.firstapp.firstproject.SearchForFriends_Fragment;
//import com.firstapp.firstproject.entity.User;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//
//import java.util.ArrayList;
//import java.util.List;
//
////public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchViewHolder> implements Filterable {
//    public class SearchFriendAdapter extends RecyclerView.Adapter<com.firstapp.firstproject.adapter.SearchFriendAdapter.SearchViewHolder>{
//
//    private ArrayList<User> userArrayList;
//    private FirebaseAuth mAuth;
//    private DatabaseReference usersRef;
//    ArrayList<User> allUserArrayList;
//
//    public SearchFriendAdapter(SearchForFriends_Fragment searchForFriends_fragment, ArrayList<User> userArrayList){
//        this.userArrayList =userArrayList;
////        this.allUserArrayList = userArrayList;
////        this.userArrayList = new ArrayList<>(allUserArrayList);
//    }
//
//    @NonNull
//    @Override
//    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.all_users_display_layout, parent,false);
//
//        //mAuth = FirebaseAuth.getInstance();
//
//        return new SearchViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
//        User user = userArrayList.get(position);
//
//        holder.userFullName.setText(user.getFullName());
//        holder.userOccupation.setText(user.getOccupation());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return userArrayList.size();
//    }
//
////    @Override
////    public Filter getFilter() {
////        return usersFilter;
////    }
////
////    private final Filter usersFilter = new Filter() {
////        @Override
////        protected FilterResults performFiltering(CharSequence constraint) {
////            ArrayList<User> filterUserList = new ArrayList<>();
////            if(constraint == null || constraint.length() == 0){
////                filterUserList.addAll(allUserArrayList);
////            }
////            else{
////                String filterPattern = constraint.toString().toLowerCase().trim();
////
////                for(User user : allUserArrayList){
////                    filterUserList.add(user);
////                }
////            }
////            FilterResults results = new FilterResults();
////            results.values = filterUserList;
////            results.count = filterUserList.size();
////            return results;
////        }
////
////        @Override
////        protected void publishResults(CharSequence constraint, FilterResults results) {
////            userArrayList.clear();
////            userArrayList.addAll((ArrayList)results.values);
////            notifyDataSetChanged();
////
////        }
////    };
//
//
//    public class SearchViewHolder extends RecyclerView.ViewHolder{
//        public TextView userFullName, userOccupation;
//
//        public SearchViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            userFullName = (TextView) itemView.findViewById(R.id.all_user_profile_full_name);
//            userOccupation = (TextView) itemView.findViewById(R.id.all_user_occupation);
//        }
//    }
//
//
//
//
//
//}
