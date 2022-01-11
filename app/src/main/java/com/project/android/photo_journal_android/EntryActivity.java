package com.project.android.photo_journal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.android.photo_journal_android.models.Entry;

public class EntryActivity extends AppCompatActivity {
    int entryId;
    Entry entry;

    ImageView imageEntry;
    TextView textTitle;
    TextView textDate;
    TextView textDescription;

    Button buttonDelete;

    DatabaseHelper db = new DatabaseHelper(EntryActivity.this);

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
        setContentView(R.layout.activity_entry);

        Intent intent = getIntent();
        if (intent.hasExtra("Entry ID")) {
            entryId = intent.getIntExtra("Entry ID", 0);
        }

        imageEntry = findViewById(R.id.imageEntry);
        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textDescription = findViewById(R.id.textDescription);

        entry = db.getEntry(entryId);

        imageEntry.setImageBitmap(entry.getImage());
        textTitle.setText(entry.getTitle());
        textDate.setText(entry.getDate());
        textDescription.setText(entry.getDescription());

        buttonDelete = findViewById(R.id.buttonDelete);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(intent);

                db.deleteEntry(entryId);
            }
        });
    }
}