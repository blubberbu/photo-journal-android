package com.project.android.photo_journal_android.models;

import android.graphics.Bitmap;

public class Entry {
    private int id;
    private String userId;
    private Bitmap image;
    private String title;
    private String description;
    private String date;

    public Entry(int id, String userId, Bitmap image, String title, String description, String date) {
        this.id = id;
        this.userId = userId;
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", user id=" + userId +
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
