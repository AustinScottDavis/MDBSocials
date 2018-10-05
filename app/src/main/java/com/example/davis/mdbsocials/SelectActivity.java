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
import android.widget.Toast;

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
    ImageView tempPicture = null;
    String tempTitle = null;
    String tempDescription = null;
    String host = null;
    String date = null;
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


        findViewById(R.id.selectImage).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

        //Listeners to check if the user entered information into the text fields
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Assigns the imageview to the users selected picture and stores to picture to be uploaded to Firebase
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            tempPicture = findViewById(R.id.temppicture);
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
                if (tempDescription == null || tempPicture == null || host == null || tempTitle == null || date == null) {
                    Toast.makeText(SelectActivity.this, "Please fill in all the information.",
                            Toast.LENGTH_SHORT).show();
                } else {
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

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("host", host);
                            data.put("title", tempTitle);
                            data.put("date", date);
                            data.put("description", tempDescription);
                            data.put("interested", map);
                            data.put("ID", key);
                            ref.setValue(data);

                            Intent i = new Intent(SelectActivity.this, FeedActivity.class);
                            SelectActivity.this.startActivityForResult(i, 1);
                        }
                    });
                }
                break;
        }
    }
}
