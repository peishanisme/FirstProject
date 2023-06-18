package com.firstapp.firstproject.entity;

import java.util.Map;

public class Post {
    private String uid;
    private String date;
    private String time;
    private String description;
    private String postImage;
    private String profileImage;
    private String fullName;

    // Create constructors, getters, and setters for the class


    public String getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Optional: You can override the toString() method to get a string representation of the object
    @Override
    public String toString() {
        return "Post{" +
                "uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", postImage='" + postImage + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}

