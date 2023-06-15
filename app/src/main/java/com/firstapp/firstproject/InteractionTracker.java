package com.firstapp.firstproject;
import android.os.Build;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class InteractionTracker {

    public InteractionTracker() {
    }

    static LinkedList <String> interactionList = new LinkedList<>();
    static LinkedList <String> interactionLinkList = new LinkedList<>();
    static LinkedList <String> interactionTimeList = new LinkedList<>();

    public static void add(String interaction, String interactionLink){
        interactionList.add(interaction);
        interactionLinkList.add(interactionLink);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            interactionTimeList.add(LocalDateTime.now().toString());
        }

    }

    public static void add(String interaction){
        add(interaction,null);
    }


    public static void clear(){
        interactionList.clear();
        interactionTimeList.clear();
        interactionLinkList.clear();
    }

}