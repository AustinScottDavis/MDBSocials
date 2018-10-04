package com.example.davis.mdbsocials;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Post> allPosts = new ArrayList<>();
    private RecyclerView.Adapter postAdapter;
    public DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getActionBar().setIcon(R.drawable.mdb_logo);
        RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //postRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager postLayoutManager = new LinearLayoutManager(this);
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
                    HashMap<String, Boolean> map = (HashMap<String, Boolean>) child.child("interested").getValue();
                    Post p = new Post(currentTitle, currentDescription, currentHost, currentDate, currentID, map);
                    allPosts.add(p);
                }
                Collections.reverse(allPosts);
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.newPost).setOnClickListener(this);
        findViewById(R.id.signOut).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //allPosts.clear();
        }*/
    }

    public void signOut() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        MainActivity.mAuth.signOut();
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.newPost:
                Intent i = new Intent(FeedActivity.this, SelectActivity.class);
                FeedActivity.this.startActivityForResult(i, 1);
                break;
            case R.id.signOut:
                signOut();
                break;
        }
    }
}
