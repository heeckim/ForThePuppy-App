package com.cookandroid.forthepuppy.utils.walkUtils;

public class WalkTab2Item {
    private String date;
    private String time;
    private String km;
    private String min;

    public WalkTab2Item(){

    }
    public WalkTab2Item(String date, String time, String km, String min) {
        this.date = date;
        this.time = time;
        this.km = km;
        this.min = min;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
