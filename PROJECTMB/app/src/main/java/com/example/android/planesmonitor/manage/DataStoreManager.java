package com.example.android.planesmonitor.manage;

import android.content.Context;
import android.util.Log;

import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.domain.PlaneActivity;
import com.example.android.planesmonitor.domain.PlaneActivityTime;
import com.example.android.planesmonitor.realm.RealmPlaneActivity;
import com.example.android.planesmonitor.util.FirebaseNames;
import com.example.android.planesmonitor.util.HashMapToObjectConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.realm.Realm;


public final class DataStoreManager {
    private static final String TAG = "DataStoreManager";
    private static DataStoreManager INSTANCE;

    private Set<String> planeNames;
    private List<RealmPlaneActivity> planeActivities;

    private int id;

    private Realm realm;
    private Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private DatabaseReference userActivitiesRef;

    public DataStoreManager(Context context) {
        this.context = context;
        planeNames = new HashSet<>();
//        realm = Realm.getInstance(context);
//        loadData();
    }

//    public static DataStoreManager getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new DataStoreManager();
//        }
//        return INSTANCE;
//    }

    public void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String userEmail = PlanesMonitorApp.getInstance().getUserEmail();
        if (!userEmail.equals("")) {
            userRef = firebaseDatabase.getReference(FirebaseNames.getInfoName(userEmail));
            userActivitiesRef = firebaseDatabase.getReference(FirebaseNames.getActivitiesName(userEmail));
            loadData();
        }
    }

    private void loadData() {
        userActivitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<HashMap<String, Object>> maps = (List<HashMap<String, Object>>)dataSnapshot.getValue();
                if (maps == null) {
                    maps = new ArrayList<HashMap<String, Object>>();
                }
                planeActivities = HashMapToObjectConverter.convertHashMapListToObjectList(maps);
                for (RealmPlaneActivity realmPlaneActivity : planeActivities) {
                    if (realmPlaneActivity.getId() > id) {
                        id = realmPlaneActivity.getId();
                    }
                    planeNames.add(realmPlaneActivity.getPlaneName());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
//        realm = Realm.getInstance(context);
//        realm.beginTransaction();
//        try {
//            id = realm.where(RealmPlaneActivity.class).max("id").intValue();
//            for (RealmPlaneActivity realmPlaneActivity : realm.where(RealmPlaneActivity.class).findAll()) {
//                planeNames.add(realmPlaneActivity.getPlaneName());
//            }
//        } finally {
//            realm.commitTransaction();
//            realm.close();
//        }
    }

    public Set<String> getPlaneNames() {
        return planeNames;
    }


    public void savePlaneActivity(PlaneActivity planeActivity) {
        if (!planeNames.contains(planeActivity.getPlaneName())) {
            planeNames.add(planeActivity.getPlaneName());
        }
        if (planeActivity.getId() <= 0) {
            planeActivity.setId(generateNextId());
            planeActivities.add(new RealmPlaneActivity(planeActivity));
            userActivitiesRef.setValue(planeActivities);
//            realm = Realm.getInstance(context);
//            realm.beginTransaction();
//            try {
//                realm.copyToRealm(new RealmPlaneActivity(planeActivity));
//            } finally {
//                realm.commitTransaction();
//                realm.close();
//            }
        } else {
            for (Iterator<RealmPlaneActivity> it = planeActivities.iterator(); it.hasNext();) {
                if (it.next().getId() == planeActivity.getId()) {
                    it.remove();
                    break;
                }
            }
            planeActivities.add(new RealmPlaneActivity(planeActivity));
            userActivitiesRef.setValue(planeActivities);
//            realm = Realm.getInstance(context);
//            realm.beginTransaction();
//            try {
//                realm.where(RealmPlaneActivity.class).equalTo("id", planeActivity.getId()).findAll().clear();
//                realm.copyToRealm(new RealmPlaneActivity(planeActivity));
//            } finally {
//                realm.commitTransaction();
//                realm.close();
//            }
        }
    }

    public List<PlaneActivity> getPlaneActivities() {
        List<PlaneActivity> realPlaneActivities = new ArrayList<>();
        for (RealmPlaneActivity realmPlaneActivity : planeActivities) {
            realPlaneActivities.add(new PlaneActivity(realmPlaneActivity));
        }
        return realPlaneActivities;
//        realm = Realm.getInstance(context);
//        realm.beginTransaction();
//        try {
//            List<PlaneActivity> planeActivities = new ArrayList<>();
//            for (RealmPlaneActivity realmPlaneActivity : realm.where(RealmPlaneActivity.class).findAll()) {
//                planeActivities.add(new PlaneActivity(realmPlaneActivity));
//            }
//            return planeActivities;
////            return realm.where(RealmPlaneActivity.class).findAll()
////                    .stream()
////                    .map(realmPlaneActivity -> new PlaneActivity(realmPlaneActivity))
////                    .collect(Collectors.toList());
//        } finally {
//            realm.commitTransaction();
//            realm.close();
//        }
    }

    private int generateNextId() {
        return ++id;
    }

    public PlaneActivity getPlaneActivityById(int id) {
        for (RealmPlaneActivity realmPlaneActivity : planeActivities) {
            if (realmPlaneActivity.getId() == id) {
                return new PlaneActivity(realmPlaneActivity);
            }
        }
        return null;
//        realm = Realm.getInstance(context);
//        realm.beginTransaction();
//        try {
//            RealmPlaneActivity realmPlaneActivity = realm.where(RealmPlaneActivity.class).equalTo("id", id).findFirst();
//            if (realmPlaneActivity == null) {
//                return null;
//            }
//            return new PlaneActivity(realmPlaneActivity);
//        } finally {
//            realm.commitTransaction();
//            realm.close();
//        }
    }

    public boolean deletePlaneActivityById(int id) {
        boolean found  = false;
        for (Iterator<RealmPlaneActivity> it = planeActivities.iterator(); it.hasNext();) {
            if (it.next().getId() == id) {
                it.remove();
                found = true;
                break;
            }
        }
        userActivitiesRef.setValue(planeActivities);
        return found;
//        realm = Realm.getInstance(context);
//        realm.beginTransaction();
//        try {
//            List<RealmPlaneActivity> realmPlaneActivities = realm.where(RealmPlaneActivity.class).equalTo("id", id).findAll();
//            if (realmPlaneActivities.size() <= 0) {
//                return false;
//            }
//            realmPlaneActivities.clear();
//            return true;
//        } finally {
//            realm.commitTransaction();
//            realm.close();
//        }
    }

    //java 8 Uses jack
    public List<PlaneActivity> getPlaneActivitiesByName(String name) {
        List<PlaneActivity> realPlaneActivities = new ArrayList<>();
        for (RealmPlaneActivity realmPlaneActivity : planeActivities) {
            if (realmPlaneActivity.getPlaneName().equals(name)) {
                realPlaneActivities.add(new PlaneActivity(realmPlaneActivity));
            }
        }
        return realPlaneActivities;
//        realm = Realm.getInstance(context);
//        realm.beginTransaction();
//        try {
//            List<PlaneActivity> planeActivities = new ArrayList<>();
//            for (RealmPlaneActivity realmPlaneActivity : realm.where(RealmPlaneActivity.class).findAll()) {
//                if (realmPlaneActivity.getPlaneName().equals(name)) {
//                    planeActivities.add(new PlaneActivity(realmPlaneActivity));
//                }
//            }
//            return planeActivities;
////            return realm.where(RealmPlaneActivity.class).findAll()
////                    .stream()
////                    .map((realmPlaneActivity -> new PlaneActivity(realmPlaneActivity)))
////                    .filter((planeActivity -> planeActivity.getPlaneName().equals(name)))
////                    .collect(Collectors.toList());
//        } finally {
//            realm.commitTransaction();
//            realm.close();
//        }
    }

    public List<PlaneActivityTime> getAllPlaneActivitiesTimes() {
        return getPlaneActivityTimes(getPlaneActivities());
    }

    public List<PlaneActivityTime> getPlaneActivitiesTimesByName(String name) {
        return getPlaneActivityTimes(getPlaneActivitiesByName(name));
    }

    private List<PlaneActivityTime> getPlaneActivityTimes(List<PlaneActivity> planeActivities) {
        List<PlaneActivityTime> planeActivityTimes = new ArrayList<>();
        if (planeActivities.size() <= 0) {
            return planeActivityTimes;
        }
        Collections.sort(planeActivities, new Comparator<PlaneActivity>() {
            @Override
            public int compare(PlaneActivity o1, PlaneActivity o2) {
                return o1.getTakeOfMoment().compareTo(o2.getTakeOfMoment());
            }
        });
        PlaneActivityTime planeActivityTime = new PlaneActivityTime();
        Iterator<PlaneActivity> it = planeActivities.iterator();
        PlaneActivity planeActivity = it.next();
        planeActivityTime.setDay(getBeginningOfDay(planeActivity.getTakeOfMoment()));
        planeActivityTime.addActivityTime(planeActivity);
        for (; it.hasNext();) {
            planeActivity = it.next();
            if (getBeginningOfDay(planeActivity.getTakeOfMoment()).compareTo(planeActivityTime.getDay()) == 0) {
                planeActivityTime.addActivityTime(planeActivity);
            } else {
                planeActivityTimes.add(planeActivityTime);
                planeActivityTime = new PlaneActivityTime();
                planeActivityTime.setDay(planeActivity.getTakeOfMoment());
                planeActivityTime.addActivityTime(planeActivity);
            }
        }
        planeActivityTimes.add(planeActivityTime);
        return planeActivityTimes;
    }

    private GregorianCalendar getBeginningOfDay(GregorianCalendar calendar) {
        return new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    }
}
