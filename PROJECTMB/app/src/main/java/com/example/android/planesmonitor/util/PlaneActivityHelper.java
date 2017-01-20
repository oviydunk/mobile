package com.example.android.planesmonitor.util;

import com.example.android.planesmonitor.domain.PlaneActivity;

import java.util.Calendar;



public class PlaneActivityHelper {
    private PlaneActivityHelper() {}

    public static String toString(PlaneActivity planeActivity) {
        return planeActivity.getPlaneName() + " " + planeActivity.getTakeOfMoment().get(Calendar.DAY_OF_MONTH) + "/" + planeActivity.getTakeOfMoment().get(Calendar.MONTH) +
                "/" + planeActivity.getTakeOfMoment().get(Calendar.YEAR) + " " + planeActivity.getTakeOfMoment().get(Calendar.HOUR) + ":" +
                String.format("%02d", planeActivity.getTakeOfMoment().get(Calendar.MINUTE)) + " - " + planeActivity.getLandingMoment().get(Calendar.HOUR) + ":" +
                String.format("%02d", planeActivity.getLandingMoment().get(Calendar.MINUTE));
    }
}
