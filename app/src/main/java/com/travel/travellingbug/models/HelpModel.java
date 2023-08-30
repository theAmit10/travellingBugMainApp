package com.travel.travellingbug.models;

public class HelpModel {
    String title;
    String description;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HelpModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public HelpModel() {
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


