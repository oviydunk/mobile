package com.example.android.planesmonitor.domain;

import com.example.android.planesmonitor.realm.RealmPlaneActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.util.Calendar.HOUR_OF_DAY;


public class PlaneActivity {
    private int id;
    private String planeName;
    private GregorianCalendar takeOfMoment;
    private GregorianCalendar landingMoment;

    public PlaneActivity() {
    }

    public PlaneActivity(int id, String planeName, GregorianCalendar takeOfMoment, GregorianCalendar landingMoment) {
        this.takeOfMoment = takeOfMoment;
        this.landingMoment = landingMoment;
        this.planeName = planeName;
        this.id = id;
    }

    public PlaneActivity(RealmPlaneActivity realmPlaneActivity) {
        id = realmPlaneActivity.getId();
        planeName = realmPlaneActivity.getPlaneName();
        takeOfMoment = new GregorianCalendar(
                realmPlaneActivity.getYear(), realmPlaneActivity.getMonth(), realmPlaneActivity.getDay(),
                realmPlaneActivity.getBeginHour(), realmPlaneActivity.getBeginMinute());
        landingMoment = new GregorianCalendar(
                realmPlaneActivity.getYear(), realmPlaneActivity.getMonth(), realmPlaneActivity.getDay(),
                realmPlaneActivity.getEndHour(), realmPlaneActivity.getEndMinute());
    }

    public GregorianCalendar getTakeOfMoment() {
        return takeOfMoment;
    }

    public void setTakeOfMoment(GregorianCalendar takeOfMoment) {
        this.takeOfMoment = takeOfMoment;
    }

    public GregorianCalendar getLandingMoment() {
        return landingMoment;
    }

    public void setLandingMoment(GregorianCalendar landingMoment) {
        this.landingMoment = landingMoment;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return planeName + " " + takeOfMoment.get(Calendar.DAY_OF_MONTH) + "/" + takeOfMoment.get(Calendar.MONTH) +
                "/" + takeOfMoment.get(Calendar.YEAR) + " " + takeOfMoment.get(HOUR_OF_DAY) + ":" +
                String.format("%02d", takeOfMoment.get(Calendar.MINUTE)) + " - " + landingMoment.get(HOUR_OF_DAY) + ":" +
                String.format("%02d", landingMoment.get(Calendar.MINUTE));
    }
}
