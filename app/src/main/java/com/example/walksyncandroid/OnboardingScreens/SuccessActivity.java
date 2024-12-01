package com.example.walksyncandroid.OnboardingScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walksyncandroid.R;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Button btnStartJourney = findViewById(R.id.btn_start_journey);

        // Go to Login Page
        btnStartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });
    }
}

