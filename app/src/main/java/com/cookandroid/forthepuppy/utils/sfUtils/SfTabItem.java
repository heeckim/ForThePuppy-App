package com.cookandroid.forthepuppy.utils.sfUtils;

public class SfTabItem {
    private String place_name;
    private String distance;
    private String place_url;
    private String road_address_name;
    private double Lng;
    private double Lat;

    public SfTabItem(String place_name, String distance, String place_url, String road_address_name, double lng, double lat) {
        this.place_name = place_name;
        this.distance = distance;
        this.place_url = place_url;
        this.road_address_name = road_address_name;
        Lng = lng;
        Lat = lat;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPlace_url() {
        return place_url;
    }

    public void setPlace_url(String place_url) {
        this.place_url = place_url;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String road_address_name) {
        this.road_address_name = road_address_name;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }
}
