package com.example.davis.mdbsocials;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    ImageView temppicture;
    String tempTitle;
    String tempDescription;
    String host;
    String date;
    Uri selectedpic;
    EditText editTitle;
    EditText editDescription;
    EditText hostName;
    EditText editDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //findViewById(R.id.tempTitle).setVisibility(View.GONE);
        editTitle = findViewById(R.id.tempTitle);
        editDescription = findViewById(R.id.tempDesc);
        hostName = findViewById(R.id.host);
        editDate = findViewById(R.id.date);
        temppicture = findViewById(R.id.temppicture);
        select();

    }

    public void select() {
        findViewById(R.id.selectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, REQUEST_CODE);
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, REQUEST_CODE);
            }
        });

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempTitle = s.toString();
            }
        });
        hostName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                host = s.toString();
            }
        });

        editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempDescription = s.toString();
            }
        });
        editDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                date = s.toString();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Post p = new Post(selectedpic, tempcaption);
//                FeedActivity.allPosts.add(p);
//                Intent i = new Intent(SelectActivity.this, ListActivity.class);
//                SelectActivity.this.startActivityForResult(i, 1);
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events").push();

                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocials-c3c00.appspot.com");
                StorageReference imageRef = storageRef.child(ref.getKey() + ".png");
                imageRef.putFile(selectedpic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        HashMap<String, Boolean> map = new HashMap<>();
                        map.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                        String key = ref.getKey();
                        ref.child("host").setValue(host);
                        ref.child("title").setValue(tempTitle);
                        ref.child("date").setValue(date);
                        ref.child("description").setValue(tempDescription);
                        ref.child("interested").setValue(map);
                        ref.child("ID").setValue(key);

                        Intent i = new Intent(SelectActivity.this, FeedActivity.class);
                        SelectActivity.this.startActivityForResult(i, 1);
                    }
                });




            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //Bundle bundle = data.getExtras();
            //temppicture.setImageBitmap((Bitmap) bundle.get("data"));
            selectedpic = data.getData();
            findViewById(R.id.tempTitle).setVisibility(View.VISIBLE);
            temppicture.setImageURI(selectedpic);
            select();
        }

    }

    public void setPicture() {
        temppicture.setImageURI(selectedpic);
    }
}
