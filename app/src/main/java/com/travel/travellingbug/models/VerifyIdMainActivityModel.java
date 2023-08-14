package com.travel.travellingbug.models;

public class VerifyIdMainActivityModel {
    String title,description,id,title_id;
    String allowed , notAllowed;

    public VerifyIdMainActivityModel() {
    }

    public VerifyIdMainActivityModel(String title, String description, String id, String title_id) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.title_id = title_id;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public String getNotAllowed() {
        return notAllowed;
    }

    public void setNotAllowed(String notAllowed) {
        this.notAllowed = notAllowed;
    }

    public String getTitle_id() {
        return title_id;
    }

    public void setTitle_id(String title_id) {
        this.title_id = title_id;
    }

    public VerifyIdMainActivityModel(String title, String description, String id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public VerifyIdMainActivityModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
