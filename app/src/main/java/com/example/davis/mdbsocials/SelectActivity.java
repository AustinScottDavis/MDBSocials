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
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 1;
    ImageView tempPicture;
    String tempTitle;
    String tempDescription;
    String host;
    String date;
    Uri selectedPic;
    EditText editTitle;
    EditText editDescription;
    EditText hostName;
    EditText editDate;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        editTitle = findViewById(R.id.tempTitle);
        editDescription = findViewById(R.id.tempDesc);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        hostName = findViewById(R.id.host);
        editDate = findViewById(R.id.date);
        tempPicture = findViewById(R.id.temppicture);

        findViewById(R.id.selectImage).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            selectedPic = data.getData();
            findViewById(R.id.tempTitle).setVisibility(View.VISIBLE);
            tempPicture.setImageURI(selectedPic);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.selectImage:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, REQUEST_CODE);
                break;
            case R.id.submit:
                progressBar.setVisibility(View.VISIBLE);
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events").push();

                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocials-c3c00.appspot.com");
                StorageReference imageRef = storageRef.child(ref.getKey() + ".png");
                imageRef.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                break;
        }
    }
}
