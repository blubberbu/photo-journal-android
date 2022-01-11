package com.project.android.photo_journal_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.android.photo_journal_android.models.Entry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddEntryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 200;

    DatabaseHelper db;

    EditText editTextTitle, editTextDesc;
    ImageView imageView;
    Button btnSave, btnBrowseImage;

    String title, description;
    Bitmap image;
    int user_id;

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
                    entry = new Entry(-1, user_id, image, title, description, "date");
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
                Bitmap imgBmp;

//                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                try {
                    imgBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(imgBmp);
                    image = imgBmp;
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
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

    public Bitmap loadImage(String filepath) {

        return BitmapFactory.decodeFile(filepath);

    }

}