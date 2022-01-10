package com.project.android.photo_journal_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.android.photo_journal_android.model.EntryModel;

public class AddEntryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 200;

    EditText editTextTitle, editTextDesc;
    ImageView imageView;
    Button btnSave, btnBrowseImage;
    DatabaseHelper db;

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

                EntryModel entryModel;

                try {
//                    Intent intent = new Intent(addEntry.this, ShowData.class);
//                    startActivity(intent);
                    entryModel = new EntryModel(-1, user_id, image, title, description);
                    Toast.makeText(AddEntryActivity.this, entryModel.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                catch(Exception e) {
                    Toast.makeText(AddEntryActivity.this, "Data is not inserted",
                            Toast.LENGTH_SHORT).show();
                    entryModel = new EntryModel(-1, 0, "error", "error", "error");
                }
                db.insertEntry(entryModel);
//                getEntry();
                Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
                startActivity(intent);
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
//                Use uri.parse to change string to Uri
                Uri finalimage = Uri.parse(image);
                if(null != selectedImageUri) {
                    imageView.setImageURI(finalimage);
                }
            }
        }
    }

    private void getEntry() {
        Cursor cursor = db.getEntry();
        if(cursor.getCount() <= 0) {
            Toast.makeText(AddEntryActivity.this, "Data is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            //buffer ganti menjadi listview
            buffer.append("Id : " + cursor.getString(0) + "\n");
            buffer.append("Title : " + cursor.getString(3) + "\n");
            buffer.append("Description : " + cursor.getString(4) + "\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Entries");
        builder.setMessage(buffer);
        builder.show();
    }
}