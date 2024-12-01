package com.example.walksyncandroid.OnboardingScreens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.DatabaseHelper;
import com.example.walksyncandroid.R;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etCalorieIntake, etWeight, etHeight, etPassword, etConfirmPassword;
    private Button btnRegister, btnCancel;

    // Database Helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        etName = findViewById(R.id.full_name);
        etEmail = findViewById(R.id.email);
        etCalorieIntake = findViewById(R.id.calorie_intake);
        etWeight = findViewById(R.id.weight);
        etHeight = findViewById(R.id.height);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);

        // Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String calorieIntake = etCalorieIntake.getText().toString().trim();
                String weight = etWeight.getText().toString().trim();
                String height = etHeight.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Validate inputs
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(calorieIntake) ||
                        TextUtils.isEmpty(weight) || TextUtils.isEmpty(height) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkEmail(email)) {
                    Toast.makeText(SignupActivity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert user into database
                boolean isInserted = databaseHelper.insertUser(name, email, calorieIntake, weight, height, password);
                if (isInserted) {
                    Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    // Clear fields
                    etName.setText("");
                    etEmail.setText("");
                    etCalorieIntake.setText("");
                    etWeight.setText("");
                    etHeight.setText("");
                    etPassword.setText("");
                    etConfirmPassword.setText("");
                    // Move to success page
                    Intent intent = new Intent(SignupActivity.this, SuccessActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel Button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the signup activity
            }
        });
    }
}
