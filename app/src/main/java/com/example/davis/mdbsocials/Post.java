package com.example.davis.mdbsocials;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;

public class Post implements Serializable{
    String title;
    String description;
    String uploader;
    String date;
    //ArrayList<String> interested;
    String ID;
    HashMap<String, Boolean> interested;
    int numLikes = 0;

    Post(String title, String description, String uploader, String date, String id, HashMap<String, Boolean> map) {
        this.title = title;
        this.description = description;
        this.uploader = uploader;
        this.date = date;
        this.ID = id;
        this.interested = map;
    }

    public void updateInterested(String id, DatabaseReference ref) {
        if (interested.containsKey(id)) {
            interested.put(id, !interested.get(id));
            ref.child("interested").setValue(interested);
        } else {
            interested.put(id, true);
            ref.child("interested").setValue(interested);
        }
        updateLikes();
    }

    public void updateLikes() {
        int i = 0;
        for (String key : interested.keySet()) {
            if (interested.get(key)) {
                i += 1;
            }
        }
        numLikes = i;
    }
}
