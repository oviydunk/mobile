package com.example.android.planesmonitor.activitiy;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.planesmonitor.R;
import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.domain.PlaneActivity;
import com.example.android.planesmonitor.exception.PlaneMonitorException;
import com.example.android.planesmonitor.manage.DataStoreManager;
import com.example.android.planesmonitor.util.GraphGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavePlaneActivity extends AppCompatActivity {
    private DataStoreManager dataStoreManager;

    @BindView(R.id.autoTxtPlane)
    public AutoCompleteTextView txtPlaneActivityName;
//    private DatePicker datePlaneActivity;
    @BindView(R.id.timeStartPlaneActivity)
    public TimePicker timeStartPlaneActivity;
    @BindView(R.id.timeEndPlaneActivity)
    public TimePicker timeEndPlaneActivity;
    @BindView(R.id.date_picker_text)
    public EditText datePickerText;
    private DatePickerDialog datePickerDialog;

    private int planeActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plane);
        if (this.getIntent().hasExtra("title")) {// this.getIntent().getExtras().containsKey("title")) {
            setTitle(this.getIntent().getExtras().getString("title"));
        } else {
            setTitle("Add Plane Activity");
        }
        ButterKnife.bind(this);

        dataStoreManager = PlanesMonitorApp.getInstance().getDataStoreManager();

        timeStartPlaneActivity.setIs24HourView(true);
        timeEndPlaneActivity.setIs24HourView(true);

        datePickerDialog = new DatePickerDialog(this);
        datePickerText.setText("" + datePickerDialog.getDatePicker().getDayOfMonth() + " / " + datePickerDialog.getDatePicker().getMonth() + " / " + datePickerDialog.getDatePicker().getYear());
        datePickerText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show();
                } else {
                    datePickerDialog.hide();
                }
            }
        });
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePickerText.setText("" + dayOfMonth + " / " + month + " / " + year);

            }
        });
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                datePickerText.clearFocus();
                timeStartPlaneActivity.requestFocus();
            }
        });

        List<String> planeNames = new ArrayList<>();
        planeNames.addAll(dataStoreManager.getPlaneNames());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, planeNames);
        txtPlaneActivityName.setAdapter(adapter);

        if (this.getIntent().hasExtra("planeActivityId")) {//this.getIntent().getExtras().containsKey("planeActivityId")) {
            planeActivityId = this.getIntent().getExtras().getInt("planeActivityId");
            PlaneActivity planeActivity = dataStoreManager.getPlaneActivityById(planeActivityId);
            if (planeActivity != null) {
                completeForm(planeActivity);
            }
        } else {
            planeActivityId = 0;
        }
    }

    public void savePlaneActivity(View view) {
        try {
            String planeName = txtPlaneActivityName.getText().toString();
            if (planeName.length() <= 0) {
                throw new PlaneMonitorException("A plane name is required");
            }
            DatePicker datePicker = datePickerDialog.getDatePicker();
            GregorianCalendar takeOfMoment = new GregorianCalendar(
                    datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    timeStartPlaneActivity.getHour(), timeStartPlaneActivity.getMinute());
            GregorianCalendar landingMoment = new GregorianCalendar(
                    datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    timeEndPlaneActivity.getHour(), timeEndPlaneActivity.getMinute());
            if (!takeOfMoment.before(landingMoment)) {
                throw new PlaneMonitorException("The start time must be before the landing time");
            }
//            dataStoreManager.addPlaneActivity(planeName, takeOfMoment, landingMoment);
            PlaneActivity planeActivity = new PlaneActivity(planeActivityId, planeName, takeOfMoment, landingMoment);
            dataStoreManager.savePlaneActivity(planeActivity);
            Toast toasty = Toast.makeText(getApplicationContext(), "Plane activity saved", Toast.LENGTH_SHORT);
            toasty.show();
//            finish();
            returnToParentActivity();

//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.setType("plain/text");
//            sendIntent.setData(Uri.parse("sergiu.c.nistor@gmail.com"));
//            sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "New Planes Activity");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, planeActivity.toString());
//            startActivity(sendIntent);

        } catch (PlaneMonitorException e) {
            Toast toasty = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toasty.show();
        }
    }

    private void completeForm(PlaneActivity planeActivity) {
        txtPlaneActivityName.setText(planeActivity.getPlaneName());
//        datePlaneActivity
        datePickerDialog.getDatePicker()
                .updateDate(
                planeActivity.getTakeOfMoment().get(Calendar.YEAR),
                planeActivity.getTakeOfMoment().get(Calendar.MONTH),
                planeActivity.getTakeOfMoment().get(Calendar.DAY_OF_MONTH)
        );
        datePickerText.setText("" + datePickerDialog.getDatePicker().getDayOfMonth() + " / " + datePickerDialog.getDatePicker().getMonth() + " / " + datePickerDialog.getDatePicker().getYear());
        timeStartPlaneActivity.setHour(planeActivity.getTakeOfMoment().get(Calendar.HOUR_OF_DAY));
        timeStartPlaneActivity.setMinute(planeActivity.getTakeOfMoment().get(Calendar.MINUTE));
        timeEndPlaneActivity.setHour(planeActivity.getLandingMoment().get(Calendar.HOUR_OF_DAY));
        timeEndPlaneActivity.setMinute(planeActivity.getLandingMoment().get(Calendar.MINUTE));

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.chart);
        GraphGenerator.populateGraphWithOnePlane(getBaseContext(), frameLayout, planeActivity.getPlaneName());
    }

    public void deletePlaneActivity(View view) {
        dataStoreManager.deletePlaneActivityById(planeActivityId);
        returnToParentActivity();
    }

    private void returnToParentActivity() {
        if (getParent() != null) {
            getParent().setResult(1);
        }
        finish();
    }
}
