package com.travel.travellingbug.models;

public class PaymentDataModel {
    String profileImage, username, time, fare, userid, requestid;

    public PaymentDataModel(String profileImage, String username, String time, String fare, String userid, String requestid) {
        this.profileImage = profileImage;
        this.username = username;
        this.time = time;
        this.fare = fare;
        this.userid = userid;
        this.requestid = requestid;
    }

    public PaymentDataModel() {
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
