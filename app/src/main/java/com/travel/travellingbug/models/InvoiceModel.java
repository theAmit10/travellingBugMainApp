package com.travel.travellingbug.models;

public class InvoiceModel {
    String image,username,rating,ratingVal,fare,seat,status,slat,slong,dlat,dlong,fromAddress,destAddress,time,vehicleDetails,bookingId;

    public InvoiceModel() {
    }

    public InvoiceModel(String image, String username, String rating, String ratingVal, String fare, String seat, String status, String slat, String slong, String dlat, String dlong, String fromAddress, String destAddress, String time, String vehicleDetails,String bookingId) {
        this.image = image;
        this.username = username;
        this.rating = rating;
        this.ratingVal = ratingVal;
        this.fare = fare;
        this.seat = seat;
        this.status = status;
        this.slat = slat;
        this.slong = slong;
        this.dlat = dlat;
        this.dlong = dlong;
        this.fromAddress = fromAddress;
        this.destAddress = destAddress;
        this.time = time;
        this.vehicleDetails = vehicleDetails;
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingVal() {
        return ratingVal;
    }

    public void setRatingVal(String ratingVal) {
        this.ratingVal = ratingVal;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSlat() {
        return slat;
    }

    public void setSlat(String slat) {
        this.slat = slat;
    }

    public String getSlong() {
        return slong;
    }

    public void setSlong(String slong) {
        this.slong = slong;
    }

    public String getDlat() {
        return dlat;
    }

    public void setDlat(String dlat) {
        this.dlat = dlat;
    }

    public String getDlong() {
        return dlong;
    }

    public void setDlong(String dlong) {
        this.dlong = dlong;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }
}

