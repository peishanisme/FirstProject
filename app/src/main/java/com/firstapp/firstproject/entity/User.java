package com.firstapp.firstproject.entity;

import java.util.ArrayList;

public class User {

    public String userid,username,email,phone_number,fullName,countryName,stateName,birthday,age,occupation,gender;
//    public ArrayList<String>hobbies;
    public User(){

    }

    public User(String userid,String username, String email, String phone_number,String fullName, String countryName, String stateName, String birthday, String age, String occupation, String gender) {
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
//        this.hobbies = hobbies;}
    }

//    public void setHobbies(String hobbies) {
//        this.hobbies = hobbies;
//    }






    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getUserid() {
        return userid;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
