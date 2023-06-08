package com.firstapp.firstproject;

public class FindFriends {

    public String ProfilePicture, fullName, occupation;

    public FindFriends(){}

    public FindFriends(String profilePicture, String fullName, String occupation) {
        ProfilePicture = profilePicture;
        this.fullName = fullName;
        this.occupation = occupation;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
