package com.example.activitytracker.services;

import androidx.annotation.NonNull;

import com.example.activitytracker.models.Record;
import com.example.activitytracker.models.State;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirebaseService {
    static Record onlineRecord;
    static DatabaseReference database;

    public static void saveRecordInFirebase() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        database = FirebaseDatabase.getInstance().getReference().child("Records").child(State.user.getUsername()).child(currentDate);
        getOnlineRecord();
    }

    private static void getOnlineRecord() {
        database.get().addOnSuccessListener(dataSnapshot -> {
            onlineRecord = dataSnapshot.getValue(Record.class);
            pushChanges();
        });
    }

    private static void pushChanges() {
        if (onlineRecord != null) {
            database.child("inVehicle").setValue(onlineRecord.getInVehicle() + State.record.getInVehicle());
            database.child("onBicycle").setValue(onlineRecord.getOnBicycle() + State.record.getOnBicycle());
            database.child("onFoot").setValue(onlineRecord.getOnFoot() + State.record.getOnFoot());
            database.child("running").setValue(onlineRecord.getRunning() + State.record.getRunning());
            database.child("still").setValue(onlineRecord.getStill() + State.record.getStill());
            database.child("tilting").setValue(onlineRecord.getTilting() + State.record.getTilting());
            database.child("unidentifiable").setValue(onlineRecord.getUnidentifiable() + State.record.getUnidentifiable());
            database.child("unknown").setValue(onlineRecord.getUnknown() + State.record.getUnknown());
            database.child("walking").setValue(onlineRecord.getWalking() + State.record.getWalking());
            database.child("username").setValue(State.record.getUsername());
        }else{
            database.child("inVehicle").setValue(State.record.getInVehicle());
            database.child("onBicycle").setValue(State.record.getOnBicycle());
            database.child("onFoot").setValue(State.record.getOnFoot());
            database.child("running").setValue(State.record.getRunning());
            database.child("still").setValue(State.record.getStill());
            database.child("tilting").setValue(State.record.getTilting());
            database.child("unidentifiable").setValue(State.record.getUnidentifiable());
            database.child("unknown").setValue(State.record.getUnknown());
            database.child("walking").setValue(State.record.getWalking());
            database.child("username").setValue(State.record.getUsername());
        }
    }
}
