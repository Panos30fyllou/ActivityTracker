package com.example.activitytracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activitytracker.R;
import com.example.activitytracker.adapters.StoredActivitiesAdapter;
import com.example.activitytracker.models.Record;
import com.example.activitytracker.models.State;
import com.example.activitytracker.models.StoredActivity;
import com.example.activitytracker.navigators.Navigator;
import com.google.firebase.auth.FirebaseAuth;
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
    private ProgressBar progressBar;
    private TextView greetingTextView;

    private String date;
    ArrayList<StoredActivity> storedActivities;
    ListView storedActivitiesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        storedActivitiesListView = findViewById(R.id.stored_activities_listview);
        CalendarView calendar = findViewById(R.id.calendarView);
        progressBar = findViewById(R.id.progressBar);
        greetingTextView = findViewById(R.id.greetingTextView);

        if(State.user == null)
            finishAffinity();
        greetingTextView.setText(new StringBuilder().append(getString(R.string.hello)).append(State.user.getFullName()).toString());
        storedActivities = new ArrayList<>();
        recordsTable = FirebaseDatabase.getInstance().getReference().child(getString(R.string.records_firebaseTableName)).child(State.user.getUsername());

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getDate());
        progressBar.setVisibility(View.VISIBLE);
        getRecordByDate();
        calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            i1++;
            date = (i2 > 9 ? String.valueOf(i2) : "0" + i2) + "-";
            date += (i1 > 9 ? String.valueOf(i1) : "0" + i1) + "-";
            date += i;
            getRecordByDate();
        });
    }

    private void getRecordByDate() {
        recordsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedActivities = new ArrayList<>();
                for (DataSnapshot activity : snapshot.child(date).getChildren()) {
                    if (!activity.getKey().equals("username"))
                        storedActivities.add(new StoredActivity(activity.getKey(), activity.getValue(Long.class)));
                }
                storedActivitiesAdapter = new StoredActivitiesAdapter(getApplicationContext(), storedActivities);
                storedActivitiesListView.setAdapter(storedActivitiesAdapter);
                if (storedActivities.size() == 0)
                    Toast.makeText(getApplicationContext(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToMain(View view) {
        Navigator.goToMain(this);
    }

    public void logout(View view) {
        State.logout();
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        Navigator.goToLogin(this);
    }
}