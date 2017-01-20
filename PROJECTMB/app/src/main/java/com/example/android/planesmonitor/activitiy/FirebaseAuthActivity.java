package com.example.android.planesmonitor.activitiy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.planesmonitor.R;
import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.util.FirebaseNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseAuthActivity extends AppCompatActivity {
    private static final String TAG = "FIREBASE";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.txt_email)
    public EditText txtEmail;

    @BindView(R.id.txt_passwd)
    public EditText txtPasswd;

    @BindView(R.id.tv_email)
    public TextView tvEmail;

    @BindView(R.id.tv_passwd)
    public TextView tvPasswd;

    @BindView(R.id.btn_sign_in)
    public Button btnSignIn;

    @BindView(R.id.btn_sign_up)
    public Button btnSignUp;

    @BindView(R.id.btn_pilot)
    public Button btnPilot;

    @BindView(R.id.btn_reg_user)
    public Button btnRegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);

        initFirebase();
        ButterKnife.bind(this);
        showUi();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPasswd.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            email += "@gmail.com";
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "createUserWithEmail:onComplete:", task.getException());
                            Toast.makeText(FirebaseAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        PlanesMonitorApp.getInstance().initFirebaseData();

                        confirmStatus();
                        // ...
                    }
                });
    }

    public void signInExistingUser(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPasswd.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT);
            return;
        }
        email = FirebaseNames.getGmailAddress(email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(FirebaseAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        PlanesMonitorApp.getInstance().initFirebaseData();
                        confirmStatus();

                        // ...
                    }
                });
    }

    private void confirmStatus() {
        final Context context = this;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getInfoName(email));
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value ==  null || value.equals("")) {
                        hideUi();
                        return;
                    }
                    Intent intent = new Intent(context, MainMenuActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        }
    }

    private void hideUi() {
        txtEmail.setVisibility(View.INVISIBLE);
        txtPasswd.setVisibility(View.INVISIBLE);
        tvEmail.setVisibility(View.INVISIBLE);
        tvPasswd.setVisibility(View.INVISIBLE);
        btnSignIn.setVisibility(View.INVISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);

        btnPilot.setVisibility(View.VISIBLE);
        btnRegUser.setVisibility(View.VISIBLE);
    }

    private void showUi() {
        txtEmail.setVisibility(View.VISIBLE);
        txtPasswd.setVisibility(View.VISIBLE);
        tvEmail.setVisibility(View.VISIBLE);
        tvPasswd.setVisibility(View.VISIBLE);
        btnSignIn.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.VISIBLE);

        btnPilot.setVisibility(View.INVISIBLE);
        btnRegUser.setVisibility(View.INVISIBLE);
    }

    public void signOut(View view) {
        showUi();
        mAuth.signOut();
        txtEmail.setText("");
        txtPasswd.setText("");
    }

    public void pilotSelected(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
            final String email = user.getEmail();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getInfoName(email));
            userRef.setValue(FirebaseNames.PILOT_ROLE);

            final DatabaseReference pilotsRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.PILOTS);
            boolean added = false;
            pilotsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    List<String> value = (List<String>) dataSnapshot.getValue();
                    if (value == null) {
                        value = new ArrayList<String>();
                    }
                    if (!value.contains(email)) {
                        value.add(email);
                        pilotsRef.setValue(value);
                    }

                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });



            showUi();

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    public void regUserSelected(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
            String email = user.getEmail();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(FirebaseNames.getInfoName(email));
            userRef.setValue(FirebaseNames.REG_USER_ROLE);

            showUi();

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }
}
