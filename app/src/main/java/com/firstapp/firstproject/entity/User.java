package com.firstapp.firstproject.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class User {

    public String userid,username,email,phone_number,fullName,countryName,stateName,birthday,age,occupation,gender,relationship;
    public List<String>hobbies = new ArrayList<>();
    public List<User>friends = new ArrayList<>();
    public User(){

    }

    public User(String userid, String username, String email, String phone_number, String fullName, String countryName, String stateName, String birthday, String age, String occupation, String gender,String relationship) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.fullName = fullName;
        this.countryName = countryName;
        this.stateName = stateName;
        this.birthday = birthday;
        this.age = age;
        this.occupation = occupation;
        this.gender = gender;
        this.relationship=relationship;
        }

    public User(String uid, String username, String fullName, String email, String phone_number) {
        this.userid=uid;
        this.username=username;
        this.fullName=fullName;
        this.email=email;
        this.phone_number=phone_number;
    }

    public List<User> getFriends() {
        return friends;
    }
    public String getUid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getBirthday() {
        return birthday;
    }


    public String getFullName() {
        return fullName;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
}

    public void setAge(String age) {
        this.age = age;
    }

    public boolean areFriend(User otherUser){
        return friends.contains(otherUser);
    }

    //Basic feature : find mutual friends between 2 user
    public List<User> getMutualFriends(User otherUser) {
        List<User> mutualFriends = new ArrayList<>();
        for (User friend : friends) {
            if (otherUser.getFriends().contains(friend)) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }

    public int getMutualFriendsNum(User otherUser){
        List<User> mutualFriends = getMutualFriends(otherUser);
        return  mutualFriends.size();
    }


    // for enhanced network

    public List<User> getSecondDegreeConnections() {
        List<User> secondDegreeConnections = new ArrayList<>();
        Queue<User> queue = new LinkedList<>();
        List<User> visited = new ArrayList<>();

        for (User friend : getFriends()) {
            queue.offer(friend);
            visited.add(friend);
        }

        while (!queue.isEmpty()) {
            User firstDeg = queue.poll();
            for (User secDeg : firstDeg.getFriends()) {
                if (!visited.contains(secDeg)) {
                    visited.add(secDeg);
                    secondDegreeConnections.add(secDeg);
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
            User secDeg = queue.poll();
            for (User thirdDeg : secDeg.getFriends()) {
                if (!visited.contains(thirdDeg) && !getFriends().contains(thirdDeg)) {
                    visited.add(thirdDeg);
                    thirdDegreeConnections.add(thirdDeg);
                }
            }
        }

        return thirdDegreeConnections;
    }
/*
    public List<User> getOtherConnections() {
        List<User> otherConnections = new ArrayList<>();
        otherConnections.remove(user);
        otherConnections.removeAll(getFriends());
        otherConnections.removeAll(getSecondDegreeConnections());
        otherConnections.removeAll(getThirdDegreeConnections());

        return otherConnections;
    }
 */

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

    public int getDegreeConnection(User otherUser){

        List<User> firstDegreeConnections = getFriends();
        if (firstDegreeConnections.contains(otherUser)) {
            return 1;
        }

        List<User> secondDegreeConnections = getSecondDegreeConnections();
        if (secondDegreeConnections.contains(otherUser)) {
            return 2;
        }

        List<User> thirdDegreeConnections = getThirdDegreeConnections();
        if (thirdDegreeConnections.contains(otherUser)) {
            return 3;
        }

        return -1; // No connection found
    }


}

