package com.travel.travellingbug.models;

public class PassengerCallModel {
    String image;
    String u_id;


    public PassengerCallModel(String image, String u_id) {
        this.image = image;
        this.u_id = u_id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
