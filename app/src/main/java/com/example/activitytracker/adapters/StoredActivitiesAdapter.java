/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.activitytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.activitytracker.R;
import com.example.activitytracker.constants.Constants;
import com.example.activitytracker.models.StoredActivity;
import com.example.activitytracker.utils.Utils;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class StoredActivitiesAdapter extends ArrayAdapter<StoredActivity> {

    private long totalTime;

    public StoredActivitiesAdapter(Context context, ArrayList<StoredActivity> storedActivities) {
        super(context, 0, storedActivities);

        totalTime = 0;
        for (StoredActivity storedActivity : storedActivities) {
            totalTime += storedActivity.getSeconds();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        StoredActivity storedActivity = getItem(position);
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.stored_activity, parent, false);

        TextView activityName = view.findViewById(R.id.stored_activity_name);
        TextView activityPercentage = view.findViewById(R.id.stored_activity_percentage);
        ProgressBar progressBar = view.findViewById(R.id.stored_activity_progress_bar);

        if (storedActivity != null) {
            activityName.setText(storedActivity.getType() + ": " + storedActivity.getSeconds() + "s");
            activityPercentage.setText(getContext().getString(R.string.percent, storedActivity.getSeconds() / totalTime));
            progressBar.setProgress(Integer.parseInt(String.valueOf(storedActivity.getSeconds() / totalTime)));
        }
        return view;
    }

    //public void updateActivities(ArrayList<DetectedActivity> detectedActivities) {
    //    HashMap<Integer, Integer> detectedActivitiesMap = new HashMap<>();
    //    for (DetectedActivity activity : detectedActivities) {
    //        detectedActivitiesMap.put(activity.getType(), activity.getConfidence());
    //    }
//
    //    ArrayList<DetectedActivity> tempList = new ArrayList<>();
    //    for (int i = 0; i < Constants.MONITORED_ACTIVITIES.length; i++) {
    //        int confidence = detectedActivitiesMap.containsKey(Constants.MONITORED_ACTIVITIES[i]) ? detectedActivitiesMap.get(Constants.MONITORED_ACTIVITIES[i]) : 0;
    //        tempList.add(new DetectedActivity(Constants.MONITORED_ACTIVITIES[i], confidence));
    //    }
//
    //    this.clear();
    //    for (DetectedActivity detectedActivity: tempList) {
    //        this.add(detectedActivity);
    //    }
    //}
}//