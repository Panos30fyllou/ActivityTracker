package com.example.activitytracker.models;


public class State {
    public static User user;
    public static boolean tracking;
    public static Record record;

    public static void logout() {
        user = null;
        tracking = false;
        record = null;
    }
}
