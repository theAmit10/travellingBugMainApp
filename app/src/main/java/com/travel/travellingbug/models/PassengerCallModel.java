package com.travel.travellingbug.models;

public class PassengerCallModel {
    String image;
    String u_id,request_id,provider_id,status,provider_status,noofseats,verification_code,total_amount,payment_status,payment_mode,user_id;


    public PassengerCallModel(String image, String request_id, String provider_id, String status, String provider_status, String noofseats, String verification_code, String total_amount, String payment_status, String payment_mode,String user_id) {
        this.image = image;
        this.request_id = request_id;
        this.provider_id = provider_id;
        this.status = status;
        this.provider_status = provider_status;
        this.noofseats = noofseats;
        this.verification_code = verification_code;
        this.total_amount = total_amount;
        this.payment_status = payment_status;
        this.payment_mode = payment_mode;
        this.user_id = user_id;
    }

    public PassengerCallModel() {

    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider_status() {
        return provider_status;
    }

    public void setProvider_status(String provider_status) {
        this.provider_status = provider_status;
    }

    public String getNoofseats() {
        return noofseats;
    }

    public void setNoofseats(String noofseats) {
        this.noofseats = noofseats;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

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
