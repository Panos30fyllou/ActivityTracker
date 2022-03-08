package com.example.activitytracker.services;

import androidx.annotation.NonNull;

import com.example.activitytracker.models.Record;
import com.example.activitytracker.models.State;
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

    public static void saveRecordInFirebase() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Records").child(State.user.getUsername()).child(currentDate);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onlineRecord = snapshot.getValue(Record.class);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
