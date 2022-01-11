package com.project.android.photo_journal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonAddEntry;

    RecyclerView rvEntries;

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
        setContentView(R.layout.activity_main);

        buttonAddEntry = findViewById(R.id.buttonAddEntry);

        buttonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivity(intent);
            }
        });

        rvEntries = findViewById(R.id.rvEntries);
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);

        ArrayList<Entry> entries = db.getEntries();

        EntriesAdapter entriesAdapter = new EntriesAdapter(MainActivity.this, entries);
        rvEntries.setLayoutManager(new LinearLayoutManager(this));
        rvEntries.setAdapter(entriesAdapter);
    }
}