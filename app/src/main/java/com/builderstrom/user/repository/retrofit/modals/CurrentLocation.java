package com.builderstrom.user.repository.retrofit.modals;

public class CurrentLocation {
    private String latitude="0.0";
    private String longitude="0.0";

    public CurrentLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CurrentLocation() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
