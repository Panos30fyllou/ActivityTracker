package com.example.activitytracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.activitytracker.R;
import com.example.activitytracker.adapters.StoredActivitiesAdapter;
import com.example.activitytracker.models.StoredActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private StoredActivitiesAdapter storedActivitiesAdapter;
    private DatabaseReference recordsTable;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ListView detectedActivitiesListView = findViewById(R.id.detected_activities_listview);
        CalendarView calendar = findViewById(R.id.calendarView);

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getDate());

        ArrayList<StoredActivity> storedActivities = new ArrayList<>();
        recordsTable = FirebaseDatabase.getInstance().getReference().child(getString(R.string.records_firebaseTableName));
        recordsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bookDataSnapshot : snapshot.getChildren()) {
                    storedActivities.add(bookDataSnapshot.getValue(StoredActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            //storedActivitiesAdapter = new StoredActivitiesAdapter(this, storedActivities);
            //detectedActivitiesListView.setAdapter(storedActivitiesAdapter);
        });
    }
}