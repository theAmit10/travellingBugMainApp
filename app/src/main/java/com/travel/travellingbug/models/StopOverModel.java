package com.travel.travellingbug.models;

public class StopOverModel {
    String lat, lng;
    String area;

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
