package com.example.walksyncandroid.HomeScreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {
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
    }
}