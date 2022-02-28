package com.example.activitytracker.navigators;

import android.app.Activity;
import android.content.Intent;

import com.example.activitytracker.activities.LoginActivity;
import com.example.activitytracker.activities.MainActivity;
import com.example.activitytracker.activities.RegisterActivity;
import com.example.activitytracker.activities.StatisticsActivity;

public class Navigator {
    public static void goToLogin(Activity activity){
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    public static void goToRegister(Activity activity){
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    public static void goToMain(Activity activity){
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
    public static void gotoStatistics (Activity activity){
        activity.startActivity(new Intent(activity, StatisticsActivity.class));
    }
}
