package com.project.android.photo_journal_android.model;

public class EntryModel {
    private int id;
    private int user_id;
    private int title;
    private int body;

    public EntryModel(int id, int user_id, int title, int body) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "EntryModel{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title=" + title +
                ", body=" + body +
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

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }
}
