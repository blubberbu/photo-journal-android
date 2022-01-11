package com.project.android.photo_journal_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.android.photo_journal_android.models.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "PhotoJournal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersQuery = "Create table users(" +
                "id INTEGER primary key autoincrement, " +
                "name text, " +
                "email text unique, " +
                "password text)";

        db.execSQL(createUsersQuery);

        String createEntriesQuery = "Create table entries(" +
                "id INTEGER primary key autoincrement, " +
                "user_id INTEGER, " +
                "image text, " +
                "title text, " +
                "description text, " + 
                "date text)";

        db.execSQL(createEntriesQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists PhotoJournal");
    }

    public boolean insertEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("E, dd/MM/yyyy, HH:mm:ss");
        String currentDate = formatter.format(new Date());

        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", entry.getUser_id());
        contentValues.put("image", entry.getImage());
        contentValues.put("title", entry.getTitle());
        contentValues.put("description", entry.getDescription());
        contentValues.put("date", currentDate);

        long result = db.insert("entries", null, contentValues);

        return !(result == -1);
    }

    public Cursor getEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from entries", null);

        return cursor;
    }

    public List<Entry> getEntriesList() {
        List<Entry> returnList = new ArrayList<>();

        String selectQuery = "SELECT * FROM entries";

        //Use getReadable instead of getWritable because writable would cause
        //  the database to lock up from other processes and cause a bottleneck
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int user_id = cursor.getInt(1);
                String image = cursor.getString(2);
                String title = cursor.getString(3);
                String description = cursor.getString(4);
                String date = cursor.getString(5);

                Entry newEntry = new Entry(id, user_id, image, title, description, date);
                returnList.add(newEntry);

            }while(cursor.moveToNext());
        }else {
            // does not add anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }
}