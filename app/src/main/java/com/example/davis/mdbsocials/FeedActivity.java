package com.example.davis.mdbsocials;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FeedActivity extends AppCompatActivity {

    public static ArrayList<Post> allPosts = new ArrayList<>();
    private RecyclerView postRecyclerView;
    private RecyclerView.Adapter postAdapter;
    private RecyclerView.LayoutManager postLayoutManager;
    public DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        postRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //postRecyclerView.setHasFixedSize(true);
        postLayoutManager = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(postLayoutManager);
        postAdapter = new PostAdapter(getApplicationContext(), allPosts);
        postRecyclerView.setAdapter(postAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("events");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allPosts.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String currentHost = child.child("host").getValue(String.class);
                    String currentDescription = child.child("description").getValue(String.class);
                    String currentDate = child.child("date").getValue(String.class);
                    String currentTitle = child.child("title").getValue(String.class);
                    String currentID = child.child("ID").getValue(String.class);

                    Post p = new Post(currentTitle, currentDescription, currentHost, currentDate, currentID);
                    allPosts.add(p);
                }
                Collections.reverse(allPosts);
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        mRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String currentHost = dataSnapshot.child("host").getValue(String.class);
//                String currentDescription = dataSnapshot.child("description").getValue(String.class);
//                String currentDate = dataSnapshot.child("date").getValue(String.class);
//                String currentTitle = dataSnapshot.child("title").getValue(String.class);
//                String currentID = dataSnapshot.child("ID").getValue(String.class);
//
//                Post p = new Post(currentTitle, currentDescription, currentHost, currentDate, currentID);
//                allPosts.add(p);
//
//                postAdapter.notifyDataSetChanged();
//                postRecyclerView.setLayoutManager(postLayoutManager);
//                postAdapter = new PostAdapter(getApplicationContext(), allPosts);
//                postRecyclerView.setAdapter(postAdapter);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        findViewById(R.id.newPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, SelectActivity.class);
                FeedActivity.this.startActivityForResult(i, 1);
            }
        });

        //run();

    }
//
//    public void run() {
//
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String currentHost = dataSnapshot.child("host").getValue(String.class);
//                String currentDescription = dataSnapshot.child("description").getValue(String.class);
//                String currentDate = dataSnapshot.child("date").getValue(String.class);
//                String currentTitle = dataSnapshot.child("title").getValue(String.class);
//                String currentID = dataSnapshot.child("ID").getValue(String.class);
//
//                Post p = new Post(currentTitle, currentDescription, currentHost, currentDate, currentID);
//                allPosts.add(p);
//
//                postAdapter.notifyDataSetChanged();
//                postRecyclerView.setLayoutManager(postLayoutManager);
//                postAdapter = new PostAdapter(getApplicationContext(), allPosts);
//                postRecyclerView.setAdapter(postAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        findViewById(R.id.newPost).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(FeedActivity.this, SelectActivity.class);
//                FeedActivity.this.startActivityForResult(i, 1);
//            }
//        });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //allPosts.clear();
        }
    }
}
