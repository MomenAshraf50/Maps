package com.example.maps;

public class ServicesLocation {
    private double lat,lng;

    public ServicesLocation() {
    }

    public ServicesLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "ServicesLocation{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
