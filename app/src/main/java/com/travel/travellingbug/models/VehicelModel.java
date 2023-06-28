package com.travel.travellingbug.models;

public class VehicelModel {
    String id,val;

    public VehicelModel(String id, String val) {
        this.id = id;
        this.val = val;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
