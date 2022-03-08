package com.example.activitytracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.activitytracker.R;
import com.example.activitytracker.adapters.StoredActivitiesAdapter;
import com.example.activitytracker.models.Record;
import com.example.activitytracker.models.State;
import com.example.activitytracker.models.StoredActivity;
import com.example.activitytracker.navigators.Navigator;
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
    ArrayList<StoredActivity> storedActivities;
    ListView storedActivitiesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        storedActivitiesListView = findViewById(R.id.stored_activities_listview);
        CalendarView calendar = findViewById(R.id.calendarView);

        storedActivities = new ArrayList<>();
        recordsTable = FirebaseDatabase.getInstance().getReference().child(getString(R.string.records_firebaseTableName)).child(State.user.getFullName());

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getDate());
        getRecordByDate();
        calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            i1++;
            date = (i2 > 9 ? String.valueOf(i2) : "0" + i2) + "-";
            date += (i1 > 9 ? String.valueOf(i1) : "0" + i1) + "-";
            date += i;
            getRecordByDate();
            //Toast.makeText(getApplicationContext(),date, Toast.LENGTH_SHORT).show();

        });
    }


    private void getRecordByDate(){
        recordsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedActivities = new ArrayList<>();
                for (DataSnapshot activity : snapshot.child(date).getChildren()) {
                    if(!activity.getKey().equals("username"))
                        storedActivities.add(new StoredActivity(activity.getKey(), activity.getValue(Long.class)));
                }
                storedActivitiesAdapter = new StoredActivitiesAdapter(getApplicationContext(), storedActivities);
                storedActivitiesListView.setAdapter(storedActivitiesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToMain(View view) {
        Navigator.goToMain(this);
    }
}