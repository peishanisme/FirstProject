//package com.firstapp.firstproject;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//*
// * A simple {@link Fragment} subclass.
// * Use the {@link Friend_Fragment#newInstance} factory method to
// * create an instance of this fragment.
//
//
//public class Friend_Fragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private NavController navController;
//    private BottomNavigationView bottomNavigationView;
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public Friend_Fragment() {
//        // Required empty public constructor
//    }
//
//*
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddFriend_Fragment.
//
//
//    // TODO: Rename and change types and number of parameters
//    public static Friend_Fragment newInstance(String param1, String param2) {
//        Friend_Fragment fragment = new Friend_Fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_friend_, container, false);
//
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//
//        // Set up the BottomNavigationView
//        bottomNavigationView = view.findViewById(R.id.bottomNavView);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            // Handle the item selection
//
//            int id = item.getItemId();
//            if(id==R.id.SearchForFriends){
//                navController.navigate(R.id.SearchForFriends);
//                return true;
//            }
//            else if(id == R.id.RequestTab){
//                navController.navigate(R.id.RequestTab);
//                return true;
//
//            } else if (id == R.id.FriendsList) {
//                navController.navigate(R.id.FriendsList);
//                return true;
//
//            }
//
//
//*switch (item.getItemId()) {
//             case R.id.SearchForFriends:
//             navController.navigate(R.id.SearchForFriends);
//             return true;
//             case R.id.RequestTab:
//             navController.navigate(R.id.RequestTab);
//             return true;
//             case R.id.FriendsList:
//             navController.navigate(R.id.FriendsList);
//             return true;
//             }*
//
//
//
//            return false;
//        });
//
//        return view;
//
//    }
//
//}
