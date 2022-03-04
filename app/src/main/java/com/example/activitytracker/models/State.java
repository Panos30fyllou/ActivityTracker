package com.example.activitytracker.models;

import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class State {
    public static User user;
    public static boolean monitoring;
    public static Record record;

    public static void updateRecord(ArrayList<DetectedActivity> detectedActivities, long elapsedTime) {
        for (DetectedActivity detectedActivity : detectedActivities) {
            if (detectedActivity.getConfidence() > 75) {
                switch (detectedActivity.getType()) {
                    case DetectedActivity.IN_VEHICLE:
                        State.record.setInVehicle(State.record.getInVehicle() + elapsedTime);
                        break;
                    case DetectedActivity.ON_BICYCLE:
                        State.record.setOnBicycle(State.record.getOnBicycle() + elapsedTime);
                        break;
                    case DetectedActivity.ON_FOOT:
                        State.record.setOnFoot(State.record.getOnFoot() + elapsedTime);
                        break;
                    case DetectedActivity.RUNNING:
                        State.record.setRunning(State.record.getRunning() + elapsedTime);
                        break;
                    case DetectedActivity.STILL:
                        State.record.setStill(State.record.getStill() + elapsedTime);
                        //Toast.makeText(this, String.valueOf(State.record.getStill()), Toast.LENGTH_SHORT);
                        break;
                    case DetectedActivity.WALKING:
                        State.record.setWalking(State.record.getInVehicle() + elapsedTime);
                        break;
                    case DetectedActivity.UNKNOWN:
                        State.record.setUnknown(State.record.getUnknown() + elapsedTime);
                        break;
                    case DetectedActivity.TILTING:
                        State.record.setTilting(State.record.getTilting() + elapsedTime);
                        break;
                }
            }
        }
    }
}
