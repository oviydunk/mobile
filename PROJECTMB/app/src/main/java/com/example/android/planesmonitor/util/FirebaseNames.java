package com.example.android.planesmonitor.util;



public final class FirebaseNames {
    public static final String PILOT_ROLE = "pilot";
    public static final String REG_USER_ROLE = "reg_user";
    public static final String PILOTS = "pilots";
    public static final String REG_USERS = "reg_users";

    public static String getInfoName(String email) {
        email = email.split("@")[0];
        return email + "-info";
    }

    public static String getActivitiesName(String email) {
        email = email.split("@")[0];
        return email + "-activities";
    }

    public static String getUserRole(String firebaseUserRole) {
        if (firebaseUserRole.equals(PILOT_ROLE)) {
            return "Pilot";
        }
        return "Regular User";
    }

    public static String getPilotName(String email) {
        email = email.split("@")[0];
        return email + "-pilot";
    }

    public static String getGmailAddress(String email) {
        if (!email.contains("@")) {
            email += "@gmail.com";
        }
        return email;
    }

    public static String getPasengersName(String email) {
        email = email.split("@")[0];
        return email + "-pasengers";
    }
}
