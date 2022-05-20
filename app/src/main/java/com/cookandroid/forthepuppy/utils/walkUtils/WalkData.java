package com.cookandroid.forthepuppy.utils.walkUtils;

import java.sql.Date;
import java.sql.Time;

public class WalkData {
    private Date date;
    private Time startTime;
    private Time endTime;
    private Double totalDistance;
    private long totalTime; // 테이블에서 수정 필요 double임
    private String location; // 이건 안쓸거같은...

    public WalkData(Date date, Time startTime, Time endTime, Double totalDistance, long totalTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
    }

    public WalkData(Date date, Time startTime, Time endTime, Double totalDistance, long totalTime, String location) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
