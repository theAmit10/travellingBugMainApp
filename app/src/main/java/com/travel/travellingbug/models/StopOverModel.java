package com.travel.travellingbug.models;

public class StopOverModel {
    String lat, lng;
    String area;

    String title,description,id,title_id;
    String allowed , notAllowed;

    public StopOverModel() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle_id() {
        return title_id;
    }

    public void setTitle_id(String title_id) {
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

    public StopOverModel(String lat, String lng, String area) {
        this.lat = lat;
        this.lng = lng;
        this.area = area;
    }

    public StopOverModel(String area) {
        this.area = area;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
