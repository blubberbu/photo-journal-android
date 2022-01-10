package com.project.android.photo_journal_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "Entries", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table users(" +
                "id int primary key, " +
                "name string, " +
                "email string, " +
                "password string, " +
                "role string)");

        db.execSQL("Create table entries(" +
                "id int primary key, " +
                "user_id int, " +
                "image string, " +
                "title text, " +
                "description text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists entries");
    }

    public boolean insertEntry(String id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("description", description);


        long hasil = db.insert("entries", null, contentValues);
        if(hasil == -1) {
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from entries", null);
        return cursor;
    }
}