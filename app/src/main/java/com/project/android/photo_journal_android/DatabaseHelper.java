package com.project.android.photo_journal_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.android.photo_journal_android.models.Entry;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "PhotoJournal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersQuery = "Create table users(" +
                "id INTEGER primary key autoincrement, " +
                "name string, " +
                "email string, " +
                "password string, " +
                "role string)";

        db.execSQL(createUsersQuery);

        String createEntriesQuery = "Create table entries(" +
                "id INTEGER primary key autoincrement, " +
                "user_id int, " +
                "image string, " +
                "title text, " +
                "description text)";

        db.execSQL(createEntriesQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists PhotoJournal");
    }

    public boolean insertEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", entry.getUser_id());
        contentValues.put("image", entry.getImage());
        contentValues.put("title", entry.getTitle());
        contentValues.put("description", entry.getDescription());

        long result = db.insert("entries", null, contentValues);

        return !(result == -1);
    }

    public Cursor getEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from entries", null);

        return cursor;
    }
}