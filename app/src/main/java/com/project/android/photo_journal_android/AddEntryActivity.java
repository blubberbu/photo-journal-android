package com.project.android.photo_journal_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.android.photo_journal_android.models.Entry;

public class AddEntryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 200;

    DatabaseHelper db;

    EditText editTextTitle, editTextDesc;
    ImageView imageView;
    Button btnSave, btnBrowseImage;

    String title, description, image;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        imageView = findViewById(R.id.imageView);
        btnSave = findViewById(R.id.btnSave);
        btnBrowseImage = findViewById(R.id.btnBrowseImage);

        db = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnBrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTextTitle.getText().toString();
                description = editTextDesc.getText().toString();

                user_id = 0;

                Entry entry;

                if (title.length() < 1 || description.length() < 1 || image == null) {
                    Toast.makeText(AddEntryActivity.this, "Please fill the form properly.", Toast.LENGTH_SHORT).show();
                } else {
                    entry = new Entry(-1, user_id, image, title, description);
                    db.insertEntry(entry);

                    Toast.makeText(AddEntryActivity.this, "Your entry has been posted.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImageUri = data.getData();
                image = selectedImageUri.toString();

                // Use uri.parse to change string to Uri
                Uri finalimage = Uri.parse(image);

                if (null != selectedImageUri) {
                    imageView.setImageURI(finalimage);
                }
            }
        }
    }
}