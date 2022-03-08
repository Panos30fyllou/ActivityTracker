package com.example.activitytracker.activities;

import static com.example.activitytracker.services.FirebaseService.saveRecordInFirebase;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.VolumeShaper;
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

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context context;
    private ActivityRecognitionClient activityRecognitionClient;
    private Button requestActivityUpdatesButton;
    private Button removeActivityUpdatesButton;
    private ListView detectedActivitiesListView;
    private DetectedActivitiesAdapter detectedActivitiesAdapter;
    Stopwatch stopwatch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        context = this;

        State.user = new User("panos", "panos", "panos");
        State.record = new Record();

        findUI();

        stopwatch = Stopwatch.createStarted();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PackageManager.PERMISSION_GRANTED);

        setButtonsEnabledState();

        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_DETECTED_ACTIVITIES, ""));
        detectedActivitiesAdapter = new DetectedActivitiesAdapter(this, detectedActivities);
        detectedActivitiesListView.setAdapter(detectedActivitiesAdapter);
        activityRecognitionClient = new ActivityRecognitionClient(this);
    }

    private void findUI() {
        requestActivityUpdatesButton = findViewById(R.id.monitorButton);
        removeActivityUpdatesButton = findViewById(R.id.stopButton);
        detectedActivitiesListView = findViewById(R.id.detected_activities_listview);
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
        State.monitoring = false;
        super.onPause();
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        requestActivityUpdates();
    }

    private void requestActivityUpdates() {
        stopwatch.reset();
        stopwatch.start();
        Task<Void> task = activityRecognitionClient.requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getActivityDetectionPendingIntent());
        task.addOnSuccessListener(result -> {
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
        removeActivityUpdates();
    }

    private void removeActivityUpdates() {
        stopwatch.stop();
        Task<Void> task = activityRecognitionClient.removeActivityUpdates(getActivityDetectionPendingIntent());
        task.addOnSuccessListener(result -> {
            Toast.makeText(context, getString(R.string.activity_updates_removed), Toast.LENGTH_SHORT);
            State.monitoring = false;
            setUpdatesRequestedState(false);
            updateDetectedActivitiesList();
            saveRecordInFirebase();
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
        if (getUpdatesRequestedState()) {
            requestActivityUpdatesButton.setEnabled(false);
            removeActivityUpdatesButton.setEnabled(true);
        } else {
            requestActivityUpdatesButton.setEnabled(true);
            removeActivityUpdatesButton.setEnabled(false);
        }
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
        for (DetectedActivity detectedActivity : detectedActivities) {
            if (detectedActivity.getConfidence() > 75) {
                switch (detectedActivity.getType()) {
                    case DetectedActivity.IN_VEHICLE:
                        State.record.setInVehicle(State.record.getInVehicle() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.ON_BICYCLE:
                        State.record.setOnBicycle(State.record.getOnBicycle() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.ON_FOOT:
                        State.record.setOnFoot(State.record.getOnFoot() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.RUNNING:
                        State.record.setRunning(State.record.getRunning() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.STILL:
                        State.record.setStill(State.record.getStill() + stopwatch.elapsed(TimeUnit.SECONDS));
                        //Toast.makeText(this, String.valueOf(State.record.getStill()), Toast.LENGTH_SHORT).show();
                        break;
                    case DetectedActivity.WALKING:
                        State.record.setWalking(State.record.getInVehicle() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.UNKNOWN:
                        State.record.setUnknown(State.record.getUnknown() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                    case DetectedActivity.TILTING:
                        State.record.setTilting(State.record.getTilting() + stopwatch.elapsed(TimeUnit.SECONDS));
                        break;
                }
            }
        }
        detectedActivitiesAdapter.updateActivities(detectedActivities);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s == Constants.KEY_DETECTED_ACTIVITIES)
            requestActivityUpdates();
    }

    public void goToStatistics(View view) {
        Navigator.gotoStatistics(this);
    }

    public void changeLanguage(View view) {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("MyLang", "");
        if(language.equals("en")) {
            setLocale("el");
            recreate();
        }else{
            setLocale("en");
            recreate();
        }
    }

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("MyLang", s);
        editor.apply();
    }

    private void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("MyLang", "");
        setLocale(language);
    }
}