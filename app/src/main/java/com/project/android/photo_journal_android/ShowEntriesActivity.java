package com.project.android.photo_journal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class ShowEntriesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_show_entries);

        rvEntries = findViewById(R.id.rvEntries);
        DatabaseHelper db = new DatabaseHelper(ShowEntriesActivity.this);

        ArrayList<Entry> entries = db.getEntriesArray();

        EntriesAdapter entriesAdapter = new EntriesAdapter(ShowEntriesActivity.this, entries);
        rvEntries.setLayoutManager(new LinearLayoutManager(this));
        rvEntries.setAdapter(entriesAdapter);

//        rvEntries.getOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ShowEntriesActivity.this, "Entry ID: " + ((Entry)entriesAdapter.getItem(position).getID), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}