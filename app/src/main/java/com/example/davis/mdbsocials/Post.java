package com.example.davis.mdbsocials;

import android.net.Uri;

import java.util.ArrayList;

public class Post {
    String title;
    String description;
    String uploader;
    String date;
    ArrayList<String> interested;

    public Post(String title, String description, String uploader, String date) {
        this.title = title;
        this.description = description;
        this.uploader = uploader;
        this.date = date;
    }
}
