package com.firstapp.firstproject;

public class FindFriends {

    public String ProfilePicture, FullName, status;

    public FindFriends(){}

    public FindFriends(String profilePicture, String fullName, String status) {
        ProfilePicture = profilePicture;
        FullName = fullName;
        this.status = status;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public String getFullName() {
        return FullName;
    }

    public String getStatus() {
        return status;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
