package com.project.android.photo_journal_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.android.photo_journal_android.models.Entry;

import java.io.IOException;

public class AddEntryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 201;
    private static final int CAMERA_PERMISSION_CODE = 202;
    private static final int PERMISSION_CODE = 500;

    DatabaseHelper db;

    EditText editTextTitle, editTextDescription;
    ImageView imageView;
    Button buttonSave, buttonBrowse, buttonCamera;

    String title, description;
    Bitmap image;
    Uri imageUri;
    int userId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // if logged in account
        inflater.inflate(R.menu.menu_account, menu);

        // else if guest
//        inflater.inflate(R.menu.menu_account_guest, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageView = findViewById(R.id.imageView);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBrowse = findViewById(R.id.buttonBrowse);
        buttonCamera = findViewById(R.id.buttonCamera);

        db = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if os is marshmallow or later, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request permission
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTextTitle.getText().toString();
                description = editTextDescription.getText().toString();

                userId = 0; // temporary

                Entry entry;

                if (title.length() < 1 || description.length() < 1 || image == null) {
                    Toast.makeText(AddEntryActivity.this, "Please fill the form properly.", Toast.LENGTH_SHORT).show();
                } else {
                    entry = new Entry(-1, userId, image, title, description, "date");
                    db.insertEntry(entry);

                    Toast.makeText(AddEntryActivity.this, "Your entry has been posted.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void openCamera() {

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "image");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "image from camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                Uri selectedImage = data.getData();

                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_REQUEST_CODE) {
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageView.setImageBitmap(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

