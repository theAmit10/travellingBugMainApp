package com.travel.travellingbug.models;

public class SearchHistoryModel {
    String fromAddress;
    String destAddress;
    String passenger;

    String slat;
    String slong;

    String dlat;
    String dlong;

    public SearchHistoryModel(String fromAddress, String destAddress, String passenger, String slat, String slong, String dlat, String dlong) {
        this.fromAddress = fromAddress;
        this.destAddress = destAddress;
        this.passenger = passenger;
        this.slat = slat;
        this.slong = slong;
        this.dlat = dlat;
        this.dlong = dlong;
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

    public SearchHistoryModel(String fromAddress, String destAddress, String passenger) {
        this.fromAddress = fromAddress;
        this.destAddress = destAddress;
        this.passenger = passenger;
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

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }
}
