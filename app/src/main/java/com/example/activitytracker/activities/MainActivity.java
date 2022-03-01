package com.example.activitytracker.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.activitytracker.R;
import com.example.activitytracker.navigators.Navigator;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected static final String TAG = "MainActivity";
    private Context mContext;

    private ActivityRecognitionClient mActivityRecognitionClient;

    private Button mRequestActivityUpdatesButton;
    private Button mRemoveActivityUpdatesButton;


    private DetectedActivitiesAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mRequestActivityUpdatesButton = findViewById(R.id.monitorButton);
        mRemoveActivityUpdatesButton = findViewById(R.id.stopButton);

        ListView detectedActivitiesListView = (ListView) findViewById(R.id.detected_activities_listview);

        setButtonsEnabledState();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PackageManager.PERMISSION_GRANTED);

        }

        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(this).getString(
                        Constants.KEY_DETECTED_ACTIVITIES, ""));

        mAdapter = new DetectedActivitiesAdapter(this, detectedActivities);
        detectedActivitiesListView.setAdapter(mAdapter);

        mActivityRecognitionClient = new ActivityRecognitionClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        updateDetectedActivitiesList();
    }

    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(result -> {
            Toast.makeText(mContext,
                    getString(R.string.activity_updates_enabled),
                    Toast.LENGTH_SHORT)
                    .show();
            setUpdatesRequestedState(true);
            updateDetectedActivitiesList();
        });

        task.addOnFailureListener(e -> {
            Log.w(TAG, getString(R.string.activity_updates_not_enabled));
            Toast.makeText(mContext,
                    getString(R.string.activity_updates_not_enabled),
                    Toast.LENGTH_SHORT)
                    .show();
            setUpdatesRequestedState(false);
        });
    }

    public void removeActivityUpdatesButtonHandler(View view) {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                getActivityDetectionPendingIntent());
        task.addOnSuccessListener(result -> {
            Toast.makeText(mContext,
                    getString(R.string.activity_updates_removed),
                    Toast.LENGTH_SHORT)
                    .show();
            setUpdatesRequestedState(false);
            mAdapter.updateActivities(new ArrayList<>());
        });

        task.addOnFailureListener(e -> {
            Log.w(TAG, "Failed to enable activity recognition.");
            Toast.makeText(mContext, getString(R.string.activity_updates_not_removed),
                    Toast.LENGTH_SHORT).show();
            setUpdatesRequestedState(true);
        });
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setButtonsEnabledState() {
        if (getUpdatesRequestedState()) {
            mRequestActivityUpdatesButton.setEnabled(false);
            mRemoveActivityUpdatesButton.setEnabled(true);
        } else {
            mRequestActivityUpdatesButton.setEnabled(true);
            mRemoveActivityUpdatesButton.setEnabled(false);
        }
    }

    private boolean getUpdatesRequestedState() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, false);
    }

    private void setUpdatesRequestedState(boolean requesting) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.KEY_ACTIVITY_UPDATES_REQUESTED, requesting)
                .apply();
        setButtonsEnabledState();
    }


    protected void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString(Constants.KEY_DETECTED_ACTIVITIES, ""));

        mAdapter.updateActivities(detectedActivities);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s == Constants.KEY_DETECTED_ACTIVITIES) {
            updateDetectedActivitiesList();
        }
    }

    public void goToStatistics(View view) {
        Navigator.gotoStatistics(this);
    }
}