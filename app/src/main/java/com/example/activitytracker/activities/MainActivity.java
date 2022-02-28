package com.example.activitytracker.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.activitytracker.R;
import com.example.activitytracker.models.State;
import com.example.activitytracker.navigators.Navigator;
import com.example.activitytracker.services.ActivityRecognisedService;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    ActivityRecognitionClient activityRecognitionClient;
    PendingIntent pendingIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACTIVITY_RECOGNITION ) != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACTIVITY_RECOGNITION }, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    public void monitorClicked(View view){
        if(!State.monitoring){
            activityRecognitionClient = ActivityRecognition.getClient(this);
            pendingIntent = PendingIntent.getService(MainActivity.this, 0, new Intent(MainActivity.this, ActivityRecognisedService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Task<Void> task = activityRecognitionClient.requestActivityUpdates(
                    3000,
                    pendingIntent);
            task.addOnSuccessListener(result -> Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_LONG).show());
            Toast.makeText(this,"Monitoring", Toast.LENGTH_LONG).show();
        }
        else{
            activityRecognitionClient.removeActivityUpdates(pendingIntent);
            Toast.makeText(this,"Stopped Monitoring", Toast.LENGTH_LONG).show();

        }
    }

    public void goToStatistics(View view){
        Navigator.gotoStatistics(this);
    }

}