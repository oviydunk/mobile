package com.example.android.planesmonitor.activitiy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.planesmonitor.R;
import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.domain.PlaneActivity;
import com.example.android.planesmonitor.manage.DataStoreManager;
import com.example.android.planesmonitor.util.FirebaseNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowAllActivity extends AppCompatActivity {
    private DataStoreManager dataStoreManager;
    @BindView(R.id.lstPlanes)
    public ListView planesListView;
    private List<PlaneActivity> planeActivities;

    ArrayAdapter<PlaneActivity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        setTitle("Plane Activities");
        ButterKnife.bind(this);

//        dataStoreManager = DataStoreManager.getInstance();
        dataStoreManager = PlanesMonitorApp.getInstance().getDataStoreManager();
//        planesListView = (ListView) findViewById(R.id.lstPlanes);

//        List<String> planeActivities = new ArrayList<>();
//        for (PlaneActivity planeActivity: dataStoreManager.getPlaneActivities()) {
//            planeActivities.add(planeActivity.toString());
//        }

        planeActivities = dataStoreManager.getPlaneActivities();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, planeActivities);

        planesListView.setAdapter(adapter);

        final Context context = this;

        planesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaneActivity planeActivity = (PlaneActivity) planesListView.getItemAtPosition(position);

                Intent intent = new Intent(context, SavePlaneActivity.class);

                intent.putExtra("title", "Update Plane Activity");
                intent.putExtra("planeActivityId", planeActivity.getId());

//                startActivity(intent);
                startActivityForResult(intent, 1);

            }
        });

        DatabaseReference userActivitiesRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getActivitiesName(PlanesMonitorApp.getInstance().getUserEmail()));
        userActivitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                planeActivities = dataStoreManager.getPlaneActivities();
                adapter = new ArrayAdapter<PlaneActivity>(context,
                        android.R.layout.simple_dropdown_item_1line, planeActivities);

                planesListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("A", "onActivityResult: ");
        planeActivities.clear();
        planeActivities.addAll(dataStoreManager.getPlaneActivities());
        adapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
