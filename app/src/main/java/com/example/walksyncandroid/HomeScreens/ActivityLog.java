package com.example.walksyncandroid.HomeScreens;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walksyncandroid.Adapters.ActivityAdapter;
import com.example.walksyncandroid.Models.ActivityItem;
import com.example.walksyncandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityLog extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<ActivityItem> activityItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        recyclerView = findViewById(R.id.activity_log_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityItemList = new ArrayList<>();

        // Example: Add passed data
        int steps = getIntent().getIntExtra("steps", 0);
        double distance = getIntent().getDoubleExtra("distance", 0.0);
        double speed = getIntent().getDoubleExtra("speed", 0.0);

        if (steps != 0) {
            activityItemList.add(new ActivityItem("Steps", steps + " steps"));
            activityItemList.add(new ActivityItem("Walking Distance", String.format("%.2f km", distance)));
            activityItemList.add(new ActivityItem("Speed", String.format("%.2f km/h", speed)));
        } else {
            // Dummy data for illustration
            activityItemList.add(new ActivityItem("Steps", "2930 steps"));
            activityItemList.add(new ActivityItem("Walking Distance", "5 km"));
            activityItemList.add(new ActivityItem("Speed", "10 km/hr"));
        }

        adapter = new ActivityAdapter(activityItemList);
        recyclerView.setAdapter(adapter);
    }
}
