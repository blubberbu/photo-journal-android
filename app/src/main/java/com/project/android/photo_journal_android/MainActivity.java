package com.project.android.photo_journal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAddEntry, btnShowEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddEntry = findViewById(R.id.btnAddEntry);
        btnShowEntries = findViewById(R.id.btnShowEntries);

        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivity(intent);
            }
        });

        btnShowEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowEntriesActivity.class);
                startActivity(intent);
            }
        });
    }
}