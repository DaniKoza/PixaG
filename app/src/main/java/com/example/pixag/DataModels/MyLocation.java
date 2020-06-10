package com.example.pixag.DataModels;

import java.io.Serializable;

public class MyLocation implements Serializable {
    private double longitude;
    private double latitude;

    public MyLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public MyLocation() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Longitude = " + longitude + "\n" +
                "Latitude = " + latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
