package com.travel.travellingbug.models;

public class UserProfileReviewDataModel {

    String user_rating,user_comment,first_name,avatar,time;

    public UserProfileReviewDataModel(String user_rating, String user_comment, String first_name, String avatar,String time) {
        this.user_rating = user_rating;
        this.user_comment = user_comment;
        this.first_name = first_name;
        this.avatar = avatar;
        this.time = time;
    }

    public UserProfileReviewDataModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
