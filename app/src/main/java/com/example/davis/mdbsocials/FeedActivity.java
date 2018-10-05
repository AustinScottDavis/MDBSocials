package com.example.davis.mdbsocials;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView.Adapter postAdapter;
    public DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager postLayoutManager = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(postLayoutManager);
        postAdapter = new PostAdapter(getApplicationContext(), Utils.allPosts);
        postRecyclerView.setAdapter(postAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        mRef = database.getReference("events");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utils.allPosts.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        String currentHost = child.child("host").getValue(String.class);
                        String currentDescription = child.child("description").getValue(String.class);
                        String currentDate = child.child("date").getValue(String.class);
                        String currentTitle = child.child("title").getValue(String.class);
                        String currentID = child.child("ID").getValue(String.class);
                        HashMap<String, Boolean> map = (HashMap<String, Boolean>) child.child("interested").getValue();
                        Post p = new Post(currentTitle, currentDescription, currentHost, currentDate, currentID, map);
                        Utils.allPosts.add(p);
                    } catch(Exception e) {
                        onDataChange(dataSnapshot);
                    }
                }
                Collections.reverse(Utils.allPosts);
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.newPostFAB).setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void signOut() {
        //Signs the user out and returns to the Login activity
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        LoginActivity.mAuth.signOut();
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        //Handles the onClickListeners for this activity
        switch(v.getId()) {
            case R.id.newPostFAB:
                Intent i = new Intent(FeedActivity.this, SelectActivity.class);
                FeedActivity.this.startActivityForResult(i, 1);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
