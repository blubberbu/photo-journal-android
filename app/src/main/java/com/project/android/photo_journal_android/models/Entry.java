package com.project.android.photo_journal_android.models;

import android.graphics.Bitmap;

public class Entry {
    private int id;
    private int user_id;
    private Bitmap image;
    private String title;
    private String description;
    private String date;

    public Entry(int id, int user_id, Bitmap image, String title, String description, String date) {
        this.id = id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
