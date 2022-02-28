package com.example.activitytracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognisedService extends IntentService
{
    private static final String TAG = "ActivityRecognisedServi";
    public ActivityRecognisedService() {
        super("ActivityRecognisedService");
    }

    public ActivityRecognisedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Toast.makeText(getApplicationContext(), "Handle Intent", Toast.LENGTH_LONG).show();

        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDeletedActivity(result.getProbableActivities());
        }
    }

    private void handleDeletedActivity(List<DetectedActivity> probableActivities) {
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: IN_VEHICLE" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: IN_VEHICLE" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: ON_BICYCLE" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: ON_BICYCLE" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: ON_FOOT" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: ON_FOOT" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.d(TAG, "handleDeletedActivity: RUNNING" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.STILL: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: STILL" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: STILL" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.WALKING: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: WALKING" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: WALKING" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.TILTING: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: TILTING" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: TILTING" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Toast.makeText(getApplicationContext(), "handleDeletedActivity: UNKNOWN" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "handleDeletedActivity: UNKNOWN" + activity.getConfidence());
                    break;
                }
            }
        }
    }
}
