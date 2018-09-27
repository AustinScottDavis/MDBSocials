package com.example.davis.mdbsocials;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable{
    String title;
    String description;
    String uploader;
    String date;
    ArrayList<String> interested;
    String ID;

    public Post(String title, String description, String uploader, String date, String id) {
        this.title = title;
        this.description = description;
        this.uploader = uploader;
        this.date = date;
        this.ID = id;
    }
}
