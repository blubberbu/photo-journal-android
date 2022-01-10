package com.project.android.photo_journal_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

public class ShowEntriesActivity extends AppCompatActivity {
    DatabaseHelper db;

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

        db = new DatabaseHelper(this);
    }

    private void getEntries() {
        Cursor cursor = db.getEntries();

        if (cursor.getCount() <= 0) {
            Toast.makeText(ShowEntriesActivity.this, "Data is empty", Toast.LENGTH_SHORT).show();

            return;
        }

        // temporary -> change to ListView
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
            buffer.append("ID: " + cursor.getString(0) + "\n");
            buffer.append("Title: " + cursor.getString(3) + "\n");
            buffer.append("Description: " + cursor.getString(4) + "\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("Entries");
        builder.setMessage(buffer);
        builder.show();
    }
}