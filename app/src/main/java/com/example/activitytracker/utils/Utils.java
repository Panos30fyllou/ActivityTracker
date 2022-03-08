package com.example.activitytracker.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.activitytracker.R;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    private Utils() {}

    public static String getActivityString(Context context, String name){
        Resources resources = context.getResources();
        switch(name) {
            case "inVehicle":
                return resources.getString(R.string.in_vehicle);
            case "onBicycle":
                return resources.getString(R.string.on_bicycle);
            case "onFoot":
                return resources.getString(R.string.on_foot);
            case "running":
                return resources.getString(R.string.running);
            case "still":
                return resources.getString(R.string.still);
            case "tilting":
                return resources.getString(R.string.tilting);
            case "unknown":
                return resources.getString(R.string.unknown);
            case "walking":
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity);
        }
    }
    public static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity) + detectedActivityType;
        }
    }

    public static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        return new Gson().toJson(detectedActivitiesList, type);
    }

    public static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
        Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        return detectedActivities == null ? new ArrayList<>() : detectedActivities;
    }
}