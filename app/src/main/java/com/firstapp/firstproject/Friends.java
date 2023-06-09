package com.firstapp.firstproject;

public class Friends {
    public String date;
    public String fullName, occupation;

    public Friends(){

    }

    public Friends(String date, String fullName, String occupation) {
        this.date = date;
        this.fullName = fullName;
        this.occupation = occupation;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFullName() {
        return fullName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
