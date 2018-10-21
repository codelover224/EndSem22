package com.example.krushna.endsem;

public class LocationCoordinates {
    double lat;
    double lon;

    String name;

    LocationCoordinates() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    /*public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    LocationCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
