package com.example.android.planesmonitor.util;

import com.example.android.planesmonitor.realm.RealmPlaneActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public final class HashMapToObjectConverter {
    public static RealmPlaneActivity convertHashMapToObject(HashMap<String, Object> map) {
        RealmPlaneActivity realmPlaneActivity = new RealmPlaneActivity();
        realmPlaneActivity.setBeginHour(((Long)map.get("beginHour")).intValue());
        realmPlaneActivity.setBeginMinute(((Long)map.get("beginMinute")).intValue());
        realmPlaneActivity.setDay(((Long)map.get("day")).intValue());
        realmPlaneActivity.setEndHour(((Long)map.get("endHour")).intValue());
        realmPlaneActivity.setEndMinute(((Long)map.get("endMinute")).intValue());
        realmPlaneActivity.setId(((Long)map.get("id")).intValue());
        realmPlaneActivity.setMonth(((Long)map.get("month")).intValue());
        realmPlaneActivity.setYear(((Long)map.get("year")).intValue());
        realmPlaneActivity.setPlaneName((String) map.get("planeName"));
        return realmPlaneActivity;
    }

    public static List<RealmPlaneActivity> convertHashMapListToObjectList(List<HashMap<String, Object>> maps) {
        List<RealmPlaneActivity> realmPlaneActivities = new ArrayList<>();
        for (HashMap<String, Object> map: maps) {
            if (map != null) {
                realmPlaneActivities.add(convertHashMapToObject(map));
            }
        }
        return realmPlaneActivities;
    }
}
