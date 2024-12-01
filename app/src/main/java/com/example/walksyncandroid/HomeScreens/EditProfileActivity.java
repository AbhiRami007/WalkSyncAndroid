package com.example.walksyncandroid.HomeScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.DatabaseHelper;
import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editCalorieIntake, editWeight, editGoalWeight, editHeight;
    private Button btnSave, btnCancel;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_compass) {
                // Navigate to the Compass activity
                startActivity(new Intent(EditProfileActivity.this, CompassActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Navigate to the Profile activity
                startActivity(new Intent(EditProfileActivity.this, Dashboard.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_settings) {
                // Navigate to the Settings activity
                startActivity(new Intent(EditProfileActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
        // Retrieve the logged-in user's email
        userEmail = getLoggedInUserEmail();
        if (userEmail == null) {
            Toast.makeText(this, "No logged-in user found!", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity if no user is logged in
            return;
        }

        // Initialize database helper and UI components
        databaseHelper = new DatabaseHelper(this);
        editName = findViewById(R.id.edit_full_name);
        editCalorieIntake = findViewById(R.id.edit_calorie_intake);
        editWeight = findViewById(R.id.edit_weight);
        editGoalWeight = findViewById(R.id.edit_goal_weight);
        editHeight = findViewById(R.id.edit_height);
        btnSave = findViewById(R.id.edit_btn_save);
        btnCancel = findViewById(R.id.edit_btn_cancel);

        // Load user data
        loadUserData();

        // Save button functionality
        btnSave.setOnClickListener(v -> saveUserData());

        // Cancel button functionality
        btnCancel.setOnClickListener(v -> finish());
    }

    // Method to retrieve the logged-in user's email from SharedPreferences
    private String getLoggedInUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("loggedInEmail", null);
    }

    // Method to load user data from the database
    private void loadUserData() {
        Cursor cursor = databaseHelper.getUserDetails(userEmail);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String calorieIntake = cursor.getString(cursor.getColumnIndexOrThrow("calorie_intake"));
            String weight = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
            String height = cursor.getString(cursor.getColumnIndexOrThrow("height"));

            // Populate fields
            editName.setText(name);
            editCalorieIntake.setText(calorieIntake);
            editWeight.setText(weight);
            editHeight.setText(height);
        }
        cursor.close();
    }

    // Method to save user data back to the database
    private void saveUserData() {
        String name = editName.getText().toString();
        String calorieIntake = editCalorieIntake.getText().toString();
        String weight = editWeight.getText().toString();
        String goalWeight = editGoalWeight.getText().toString();
        String height = editHeight.getText().toString();

        if (name.isEmpty() || calorieIntake.isEmpty() || weight.isEmpty() || height.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseHelper.updateUser(userEmail, name, calorieIntake, weight, goalWeight, height);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
        }
    }
}
