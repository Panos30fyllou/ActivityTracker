
package com.example.activitytracker.constants;

import com.google.android.gms.location.DetectedActivity;

public final class Constants {

    private Constants() {}

    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";

    public static final String KEY_ACTIVITY_UPDATES_REQUESTED = PACKAGE_NAME + ".ACTIVITY_UPDATES_REQUESTED";

    public static final String KEY_DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";

    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 3 * 1000; // 3 seconds

    public static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };
}
