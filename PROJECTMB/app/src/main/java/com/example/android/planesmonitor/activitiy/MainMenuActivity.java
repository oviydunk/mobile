package com.example.android.planesmonitor.activitiy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.planesmonitor.R;
import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.util.FirebaseNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity {

    @BindView(R.id.display_usr_email)
    public TextView tvEmail;

    @BindView(R.id.display_role)
    public TextView tvRole;

    @BindView(R.id.tv_pilot_name)
    public TextView tvPilotName;

    @BindView(R.id.btn_add_pilot)
    public Button btnAddPilot;

    @BindView(R.id.btn_view_pasengers)
    public Button btnViewPasengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
//        DataStoreManager.getInstance();
        ButterKnife.bind(this);
        tvRole.setText(FirebaseNames.getUserRole(PlanesMonitorApp.getInstance().getUserRole()));
        initUIDependingOnRole();
        tvEmail.setText("Signed in as: " + PlanesMonitorApp.getInstance().getUserEmail());
        makeAnimation();
    }

    private void makeAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(2000);
        tvEmail.startAnimation(animation);
    }

    private void initUIDependingOnRole() {
        String role = PlanesMonitorApp.getInstance().getUserRole();
        if (role.equals(FirebaseNames.PILOT_ROLE)) {
            btnAddPilot.setVisibility(View.INVISIBLE);
            tvPilotName.setVisibility(View.INVISIBLE);
            btnViewPasengers.setVisibility(View.VISIBLE);
        } else {
            btnViewPasengers.setVisibility(View.INVISIBLE);
            showHidePilotName();
            DatabaseReference pilotName = FirebaseDatabase.getInstance().getReference(FirebaseNames.getPilotName(PlanesMonitorApp.getInstance().getUserEmail()));
            pilotName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showHidePilotName();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
    }

    private void showHidePilotName() {
        String pilotName = PlanesMonitorApp.getInstance().getUserPilot();
        if (pilotName.equals("")) {
            btnAddPilot.setVisibility(View.VISIBLE);
            tvPilotName.setVisibility(View.INVISIBLE);
        } else {
            btnAddPilot.setVisibility(View.INVISIBLE);
            tvPilotName.setVisibility(View.VISIBLE);
            tvPilotName.setText("Pilot: " + pilotName);
        }
    }

    public void onBtnAddPlaneActivityClick(View view) {
        Intent intent = new Intent(this, SavePlaneActivity.class);
        startActivity(intent);
    }

    public void onBtnShowAllPlaneActivityClick(View view) {
        Intent intent = new Intent(this, ShowAllActivity.class);
        startActivity(intent);
    }

    public void onBtnAddPilotClick(View view) {
        Intent intent = new Intent(this, AddPilotActivity.class);
        startActivity(intent);
    }

    public void onBtnViewPasengersClick(View view) {
        Intent intent = new Intent(this, ShowAllPasengersActivity.class);
        startActivity(intent);
    }
}
