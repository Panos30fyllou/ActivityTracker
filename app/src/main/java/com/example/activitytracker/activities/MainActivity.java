package com.example.activitytracker.activities;

import static com.example.activitytracker.services.FirebaseService.saveRecordInFirebase;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.activitytracker.R;
import com.example.activitytracker.adapters.DetectedActivitiesAdapter;
import com.example.activitytracker.constants.Constants;
import com.example.activitytracker.models.Record;
import com.example.activitytracker.models.State;
import com.example.activitytracker.models.User;
import com.example.activitytracker.navigators.Navigator;
import com.example.activitytracker.services.DetectedActivitiesIntentService;
import com.example.activitytracker.utils.Utils;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Stopwatch;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected static final String TAG = "MainActivity";

    private Context context;
    private ActivityRecognitionClient activityRecognitionClient;
    private Button requestActivityUpdatesButton;
    private Button removeActivityUpdatesButton;

    Stopwatch stopwatch;

    private DetectedActivitiesAdapter detectedActivitiesAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        State.user = new User("panos", "panos", "panos");
        State.record = new Record();

        requestActivityUpdatesButton = findViewById(R.id.monitorButton);
        removeActivityUpdatesButton = findViewById(R.id.stopButton);

        ListView detectedActivitiesListView = findViewById(R.id.detected_activities_listview);

        setButtonsEnabledState();

        stopwatch = Stopwatch.createStarted();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PackageManager.PERMISSION_GRANTED);

        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_DETECTED_ACTIVITIES, ""));

        detectedActivitiesAdapter = new DetectedActivitiesAdapter(this, detectedActivities);
        detectedActivitiesListView.setAdapter(detectedActivitiesAdapter);
        activityRecognitionClient = new ActivityRecognitionClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        State.monitoring = true;
        updateDetectedActivitiesList();
    }

    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        Task<Void> task = activityRecognitionClient.requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getActivityDetectionPendingIntent());
        task.addOnSuccessListener(result -> {
            Toast.makeText(context, getString(R.string.activity_updates_enabled), Toast.LENGTH_SHORT);
            stopwatch.reset();
            stopwatch.start();
            State.monitoring = true;
            setUpdatesRequestedState(true);
            updateDetectedActivitiesList();
        });

        task.addOnFailureListener(e -> {
            Toast.makeText(context, getString(R.string.activity_updates_not_enabled), Toast.LENGTH_SHORT).show();
            setUpdatesRequestedState(false);
        });
    }

    public void removeActivityUpdatesButtonHandler(View view) {
        Task<Void> task = activityRecognitionClient.removeActivityUpdates(getActivityDetectionPendingIntent());
        task.addOnSuccessListener(result -> {
            Toast.makeText(context, getString(R.string.activity_updates_removed), Toast.LENGTH_SHORT);
            stopwatch.stop();
            State.monitoring = false;
            setUpdatesRequestedState(false);
            saveRecordInFirebase();
            updateDetectedActivitiesList();
            detectedActivitiesAdapter.updateActivities(new ArrayList<>());
        });
        task.addOnFailureListener(e -> {
            Toast.makeText(context, getString(R.string.activity_updates_not_removed), Toast.LENGTH_SHORT).show();
            setUpdatesRequestedState(true);
        });
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setButtonsEnabledState() {
        requestActivityUpdatesButton.setEnabled(!getUpdatesRequestedState());
        removeActivityUpdatesButton.setEnabled(getUpdatesRequestedState());
    }

    private boolean getUpdatesRequestedState() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, false);
    }

    private void setUpdatesRequestedState(boolean requesting) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, requesting)
                .apply();
        setButtonsEnabledState();
    }

    protected void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.KEY_DETECTED_ACTIVITIES, ""));
        State.updateRecord(detectedActivities, stopwatch.elapsed(TimeUnit.SECONDS));
        detectedActivitiesAdapter.updateActivities(detectedActivities);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s == Constants.KEY_DETECTED_ACTIVITIES)
            updateDetectedActivitiesList();
    }

    public void goToStatistics(View view) {
        Navigator.gotoStatistics(this);
    }
}