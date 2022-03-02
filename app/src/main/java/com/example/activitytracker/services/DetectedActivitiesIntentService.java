package com.example.activitytracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.example.activitytracker.utils.Utils;
import com.example.activitytracker.constants.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "DetectedActivitiesIS";

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(Constants.KEY_DETECTED_ACTIVITIES, Utils.detectedActivitiesToJson(detectedActivities))
                .apply();
    }
}
