package com.example.walksyncandroid.HomeScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.DatabaseHelper;
import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private TextView name, email, calorie_intake, weight, height, goal_weight;
    private DatabaseHelper databaseHelper;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView editProfile = findViewById(R.id.editProfile);

        // Retrieve the logged-in user's email
        userEmail = getLoggedInUserEmail();
        if (userEmail == null) {
            Toast.makeText(this, "No logged-in user found!", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity if no user is logged in
            return;
        }

        // Initialize database helper and UI components
        databaseHelper = new DatabaseHelper(this);
        name = findViewById(R.id.profileName);
        email=findViewById(R.id.profileEmail);
        calorie_intake = findViewById(R.id.calorie_intake);
        weight = findViewById(R.id.current_weight);
        goal_weight = findViewById(R.id.goal_weight);
        height = findViewById(R.id.height);

        // Load user data
        loadUserData();
        // Handle navigation item clicks
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                // Stay on the dashboard
                return true;
            } else if (item.getItemId() == R.id.navigation_compass) {
                // Navigate to the Compass activity
                startActivity(new Intent(ProfileActivity.this, CompassActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Navigate to the Profile activity
                startActivity(new Intent(ProfileActivity.this, Dashboard.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_settings) {
                // Navigate to the Settings activity
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });
    }

    // Method to retrieve the logged-in user's email from SharedPreferences
    private String getLoggedInUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("loggedInEmail", null);
        if (email == null) {
            Toast.makeText(this, "No logged-in user found!", Toast.LENGTH_SHORT).show();
        }
        return email;
    }

    // Method to load user data from the database
    private void loadUserData() {
        Cursor cursor = databaseHelper.getUserDetails(userEmail);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String tv_name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String tv_email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String tv_calorieIntake = cursor.getString(cursor.getColumnIndexOrThrow("calorie_intake"));
                String tv_weight = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
                String tv_goalWeight = cursor.getString(cursor.getColumnIndexOrThrow("goal_weight"));
                String tv_height = cursor.getString(cursor.getColumnIndexOrThrow("height"));

                // Populate fields
                name.setText(tv_name);
                email.setText(tv_email);
                calorie_intake.setText(tv_calorieIntake);
                weight.setText(tv_weight);
                goal_weight.setText(tv_goalWeight);
                height.setText(tv_height);
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(this, "No user data found!", Toast.LENGTH_SHORT).show();
        }
    }


}