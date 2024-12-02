package com.example.walksyncandroid.HomeScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox checkboxGoalNotifications, checkboxDailyNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_settings) {
                // Stay on the dashboard
                return true;
            } else if (item.getItemId() == R.id.navigation_compass) {
                // Navigate to the Compass activity
                startActivity(new Intent(SettingsActivity.this, CompassActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Navigate to the Profile activity
                startActivity(new Intent(SettingsActivity.this, Dashboard.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                // Navigate to the Settings activity
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        // Initialize checkboxes
        checkboxGoalNotifications = findViewById(R.id.checkbox_goal_notifications);
        checkboxDailyNotifications = findViewById(R.id.checkbox_daily_notifications);

        // Load saved preferences
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        checkboxGoalNotifications.setChecked(sharedPreferences.getBoolean("goal_notifications", false));
        checkboxDailyNotifications.setChecked(sharedPreferences.getBoolean("daily_notifications", false));

        // Save preferences when they change
        checkboxGoalNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("goal_notifications", isChecked);
            editor.apply();
        });

        checkboxDailyNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("daily_notifications", isChecked);
            editor.apply();
        });
    }
}