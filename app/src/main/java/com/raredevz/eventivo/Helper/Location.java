package com.raredevz.eventivo.Helper;

import java.io.Serializable;

public class Location implements Serializable {
    double latitude,longitude;

    android.location.Location gooleLocation;

    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public android.location.Location toGoogleLocation(){
        android.location.Location location=new android.location.Location("Dummy");
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }

    public String toString(){
        return "Lat:"+latitude+"\n" +
                "Long: "+longitude;
    }
}
