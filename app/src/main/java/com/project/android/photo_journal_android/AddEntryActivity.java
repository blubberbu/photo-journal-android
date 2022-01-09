package com.project.android.photo_journal_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddEntryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    EditText editTextTitle, editTextDesc;
    ImageView imageView;
    Button btnSave;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        imageView = findViewById(R.id.imageView);
        btnSave = findViewById(R.id.btnSave);
        db = new DatabaseHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "1";
                String title = editTextTitle.getText().toString();
                String description = editTextDesc.getText().toString();

                boolean isInserted = db.insertEntry(id, title,
                        description);

                if(isInserted) {
//                    Intent intent = new Intent(addEntry.this, ShowData.class);
//                    startActivity(intent);
                    getEntry();
                    Toast.makeText(AddEntryActivity.this, "Data Inserted",
                            Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(AddEntryActivity.this, "Data is not inserted",
                            Toast.LENGTH_SHORT).show();
            }
        });
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
            buffer.append("Title : " + cursor.getString(1) + "\n");
            buffer.append("Description : " + cursor.getString(2) + "\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Entries");
        builder.setMessage(buffer);
        builder.show();
    }
}