package com.firstapp.firstproject;
import android.os.Build;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class InteractionTracker {

    public InteractionTracker() {
    }

    // Linked list is be used to store interaction
    static LinkedList <String> interactionList = new LinkedList<>();
    static LinkedList <String> interactionLinkList = new LinkedList<>();
    static LinkedList <String> interactionTimeList = new LinkedList<>();

    public static void add(String interaction, String interactionLink){
        interactionList.add(interaction); // add interaction's name
        interactionLinkList.add(interactionLink); // add interaction's link like uid of the user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // get the local time
            interactionTimeList.add(LocalDateTime.now().toString()); // add time in the string form
        }

    }

    public static void add(String interaction){
        add(interaction,null);
    }


    public static void clear(){ // clear all the linked list
        interactionList.clear();
        interactionTimeList.clear();
        interactionLinkList.clear();
    }

}