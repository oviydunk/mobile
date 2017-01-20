package com.example.android.planesmonitor.domain;

import java.util.GregorianCalendar;



public class PlaneActivityTime {
    private GregorianCalendar day;
    private long totalMinutes;

    public PlaneActivityTime(GregorianCalendar day, int totalMinutes) {
        this.day = day;
        this.totalMinutes = totalMinutes;
    }

    public PlaneActivityTime() {
        this.totalMinutes = 0;
    }

    public GregorianCalendar getDay() {
        return day;
    }

    public void setDay(GregorianCalendar day) {
        this.day = day;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public void addActivityTime(PlaneActivity planeActivity) {
        this.totalMinutes += (planeActivity.getLandingMoment().getTimeInMillis() - planeActivity.getTakeOfMoment().getTimeInMillis()) / 60000;
    }
}
