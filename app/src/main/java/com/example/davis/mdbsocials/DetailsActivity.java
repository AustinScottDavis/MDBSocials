package com.example.davis.mdbsocials;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Post currentPost = (Post) intent.getSerializableExtra("post");

        TextView title = findViewById(R.id.title);
        title.setText(currentPost.title);

        TextView host = findViewById(R.id.host);
        host.setText(currentPost.uploader);

        TextView date = findViewById(R.id.date);
        date.setText(currentPost.date);

        TextView desc = findViewById(R.id.description);
        desc.setText(currentPost.description);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(currentPost.ID + ".png");

        Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(storageRef).into((ImageView) findViewById(R.id.picture));
    }
}
