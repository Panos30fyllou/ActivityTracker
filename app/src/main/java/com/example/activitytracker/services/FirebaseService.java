package com.example.activitytracker.services;

import com.example.activitytracker.models.State;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirebaseService {

    public static void saveRecordInFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Records").child(State.user.getUsername());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        database.child(currentDate).setValue(State.record);
    }
}
