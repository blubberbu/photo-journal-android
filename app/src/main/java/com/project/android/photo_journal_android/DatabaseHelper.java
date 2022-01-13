package com.project.android.photo_journal_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.project.android.photo_journal_android.models.Entry;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "PhotoJournal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createEntriesQuery = "Create table entries(" +
                "id INTEGER primary key autoincrement, " +
                "user_id text, " +
                "image BLOB, " +
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

        //Get image in byte to store into db
        Bitmap imgBmp = entry.getImage();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imgBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", entry.getUserId());
        contentValues.put("image", imageByte);
        contentValues.put("title", entry.getTitle());
        contentValues.put("description", entry.getDescription());
        contentValues.put("date", currentDate);

        long result = db.insert("entries", null, contentValues);

        return !(result == -1);
    }

    public ArrayList<Entry> getEntries(String unionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Entry> entryList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from entries where user_id=?", new String[]{unionId});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String userId = cursor.getString(1);
                byte[] imageBytesToBmp = cursor.getBlob(2);
                //Convert byte from db to bitmap for object class
                Bitmap imageBmp = BitmapFactory.decodeByteArray(imageBytesToBmp, 0, imageBytesToBmp.length);
                String title = cursor.getString(3);
                String description = cursor.getString(4);
                String date = cursor.getString(5);

                Entry newEntry = new Entry(id, userId, imageBmp, title, description, date);
                entryList.add(newEntry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return entryList;
    }

    public Entry getEntry(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from entries where id=?", new String[]{id.toString()});

        Entry entry = null;

        while (cursor.moveToNext()) {
            String userId = cursor.getString(1);
            byte[] imageBytesToBmp = cursor.getBlob(2);
            Bitmap imageBmp = BitmapFactory.decodeByteArray(imageBytesToBmp, 0, imageBytesToBmp.length);
            String title = cursor.getString(3);
            String description = cursor.getString(4);
            String date = cursor.getString(5);

            entry = new Entry(id, userId, imageBmp, title, description, date);
        }

        cursor.close();
        db.close();

        return entry;
    }

    public boolean deleteEntry(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from entries where id=?", new String[]{id.toString()});

        if (cursor.getCount() > 0) {
            long result = db.delete("entries", "id=?", new String[]{id.toString()});

            return !(result == -1);
        }

        return false;
    }
}