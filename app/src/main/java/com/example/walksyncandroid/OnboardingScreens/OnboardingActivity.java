package com.example.walksyncandroid.OnboardingScreens;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.R;

public class OnboardingActivity extends AppCompatActivity {

    private LinearLayout getStartedButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize the views
        getStartedButton = findViewById(R.id.getStartedButton);
        loginButton = findViewById(R.id.loginButton);

        // Handle Get Started button click
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity
                Intent intent = new Intent(OnboardingActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Handle Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginActivity
                Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
