package com.project.android.photo_journal_android.model;

public class EntryModel {
    private int id;
    private int user_id;
    private String image;
    private String title;
    private String description;

    public EntryModel(int id, int user_id, String image, String title, String description) {
        this.id = id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "EntryModel{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
}
