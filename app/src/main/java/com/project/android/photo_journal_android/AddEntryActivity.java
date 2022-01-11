package com.project.android.photo_journal_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
    private static final int PICK_IMAGE = 200;

    DatabaseHelper db;

    EditText editTextTitle, editTextDescription;
    ImageView imageView;
    Button buttonSave, buttonBrowse;

    String title, description;
    Bitmap image;
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

        db = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
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
                Uri selectedImage = data.getData();
                Bitmap imageBitmap;

//                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(imageBitmap);
                    image = imageBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                Toast.makeText(AddEntryActivity.this , "columnIndex " + cursor.getString(0), Toast.LENGTH_LONG).show();
//                cursor.close();

//                image = selectedImage.toString();
//
//                Uri imgUri = Uri.parse(image);
//                imageView.setImageURI(imgUri);
            }
        }
    }

//    public Bitmap loadImage(String filepath) {
//        return BitmapFactory.decodeFile(filepath);
//    }

}