package com.example.android.planesmonitor.realm;

import com.example.android.planesmonitor.domain.PlaneActivity;

import java.util.Calendar;

import io.realm.RealmObject;



public class RealmPlaneActivity extends RealmObject{
    private int id;
    private String planeName;
    private int year;
    private int month;
    private int day;
    private int beginHour;
    private int beginMinute;
    private int endHour;
    private int endMinute;

    public RealmPlaneActivity() {
    }

    public RealmPlaneActivity(PlaneActivity planeActivity) {
        id = planeActivity.getId();
        planeName = planeActivity.getPlaneName();
        year = planeActivity.getTakeOfMoment().get(Calendar.YEAR);
        month = planeActivity.getTakeOfMoment().get(Calendar.MONTH);
        day = planeActivity.getTakeOfMoment().get(Calendar.DAY_OF_MONTH);
        beginHour = planeActivity.getTakeOfMoment().get(Calendar.HOUR_OF_DAY);
        beginMinute = planeActivity.getTakeOfMoment().get(Calendar.MINUTE);
        endHour = planeActivity.getLandingMoment().get(Calendar.HOUR_OF_DAY);
        endMinute = planeActivity.getLandingMoment().get(Calendar.MINUTE);
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(int beginMinute) {
        this.beginMinute = beginMinute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
