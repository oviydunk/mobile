package com.example.android.planesmonitor.activitiy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.planesmonitor.R;
import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.util.FirebaseNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPilotActivity extends AppCompatActivity {

    private static final String TAG = "AddPilotActivity";

    @BindView(R.id.txt_pilot_email)
    public EditText txtPilotEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pilot);
        ButterKnife.bind(this);
    }

    public void addPilot(View view) {
        final Context context = this;
        final String pilotEmail = FirebaseNames.getGmailAddress(txtPilotEmail.getText().toString());
        DatabaseReference pilotsRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.PILOTS);
        pilotsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                List<String> value = (List<String>) dataSnapshot.getValue();
                if (value == null) {
                    value = new ArrayList<String>();
                }

                if (value.contains(pilotEmail)) {
                    final DatabaseReference pilotedRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getPasengersName(pilotEmail));
                    pilotedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            List<String> value = (List<String>) dataSnapshot.getValue();
                            Log.d(TAG, "Value is: " + value);
                            if (value ==  null) {
                                value = new ArrayList<String>();
                            }
                            String userEmail = PlanesMonitorApp.getInstance().getUserEmail();
                            if (!value.contains(userEmail)) {
                                value.add(userEmail);
                                pilotedRef.setValue(value);
                                DatabaseReference userPilotRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getPilotName(userEmail));
                                userPilotRef.setValue(pilotEmail);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } else {
                    Toast.makeText(context, "No pilot with this email", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
