package com.travel.travellingbug.models;

public class PassengerDataModel {
    String pname;
    String plocation;

    public PassengerDataModel(String pname, String plocation) {
        this.pname = pname;
        this.plocation = plocation;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }
}
