package com.firstapp.firstproject.entity;

import java.util.ArrayList;

public class Hobby {
    private ArrayList<String>HobbiesList = new ArrayList<>();

    public Hobby(ArrayList<String> hobbiesList) {
        HobbiesList = hobbiesList;
    }

    public Hobby() {
    }

    public void addHobbies(String hobby){
        HobbiesList.add(hobby);
    }

    public ArrayList<String> getHobbiesList() {
        return HobbiesList;
    }

    public void setHobbiesList(ArrayList<String> hobbiesList) {
        HobbiesList = hobbiesList;
    }
}
