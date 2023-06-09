package com.firstapp.firstproject;

import com.firstapp.firstproject.entity.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SocialNetwork {
    private User user;
    private List<User> allUsers;

    public SocialNetwork(User user) {
        this.user = user;
        this.allUsers = new ArrayList<>();
    }

    public void addUser(User user) {
        allUsers.add(user);
    }

    public List<User> getFirstDegreeConnections() {
        return user.getFriends();
    }


    public List<User> getSecondDegreeConnections() {
        List<User> secondDegreeConnections = new ArrayList<>();
        Queue<User> queue = new LinkedList<>();
        List<User> visited = new ArrayList<>();

        for (User friend : getFirstDegreeConnections()) {
            queue.offer(friend);
            visited.add(friend);
        }

        while (!queue.isEmpty()) {
            User currentUser = queue.poll();
            for (User friend : currentUser.getFriends()) {
                if (!visited.contains(friend) && !friend.equals(user)) {
                    visited.add(friend);
                    secondDegreeConnections.add(friend);
                }
            }
        }

        return secondDegreeConnections;
    }

    public List<User> getThirdDegreeConnections() {
        List<User> thirdDegreeConnections = new ArrayList<>();
        Queue<User> queue = new LinkedList<>();
        List<User> visited = new ArrayList<>();

        for (User secondDegreeConnection : getSecondDegreeConnections()) {
            queue.offer(secondDegreeConnection);
            visited.add(secondDegreeConnection);
        }

        while (!queue.isEmpty()) {
            User currentUser = queue.poll();
            for (User friend : currentUser.getFriends()) {
                if (!visited.contains(friend) && !getFirstDegreeConnections().contains(friend)) {
                    visited.add(friend);
                    thirdDegreeConnections.add(friend);
                }
            }
        }

        return thirdDegreeConnections;
    }

    public List<User> getOtherConnections() {
        List<User> otherConnections = new ArrayList<>(allUsers);
        otherConnections.remove(user);
        otherConnections.removeAll(getFirstDegreeConnections());
        otherConnections.removeAll(getSecondDegreeConnections());
        otherConnections.removeAll(getThirdDegreeConnections());

        return otherConnections;
    }

    public List<User> getRecommendedConnections() {
        List<User> recommendedConnections = new ArrayList<>();

        List<User> secondDegreeConnections = getSecondDegreeConnections();
        secondDegreeConnections.sort(Comparator.comparingInt(user -> -user.getFriends().size()));   //(-) to sort the list in descending order

        List<User> thirdDegreeConnections = getThirdDegreeConnections();
        thirdDegreeConnections.sort(Comparator.comparingInt(user -> -user.getFriends().size()));

        recommendedConnections.addAll(secondDegreeConnections);
        recommendedConnections.addAll(thirdDegreeConnections);

        return recommendedConnections;
    }

    public int getDegreeConnection(User user1, User user2){
        if (user1.equals(user2)) {
            return 0; // Same user, degree of connection is 0
        }

//        List<User> firstDegreeConnections = getFirstDegreeConnections(user1);
//        if (firstDegreeConnections.contains(user2)) {
//            return 1;
//        }
//
//        List<User> secondDegreeConnections = getSecondDegreeConnections(user1);
//        if (secondDegreeConnections.contains(user2)) {
//            return 2;
//        }
//
//        List<User> thirdDegreeConnections = getThirdDegreeConnections(user1);
//        if (thirdDegreeConnections.contains(user2)) {
//            return 3;
//        }

        return -1; // No connection found
    }

}
