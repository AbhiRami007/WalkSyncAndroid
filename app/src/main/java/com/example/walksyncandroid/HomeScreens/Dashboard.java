package com.example.walksyncandroid.HomeScreens;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class Dashboard extends AppCompatActivity implements SensorEventListener, LocationListener {

    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 100;
    private static final int LOCATION_REQUEST_CODE = 101;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private TextView activityStatusTextView, stepsTextView, walkingDistanceTextView, speedTextView;
    private Button startTrackingButton, activityLogButton;

    private Handler handler = new Handler();
    private int stepsCount = 0;
    private double walkingDistance = 0.0;
    private double speed = 0.0;

    private LocationManager locationManager;

    private boolean isTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean dailyNotificationsEnabled = sharedPreferences.getBoolean("daily_notifications", false);
        Log.i("TAG", "onCreate: dailyNotificationsEnabled"+dailyNotificationsEnabled);
        if (dailyNotificationsEnabled) {
            scheduleDailyReminder(this);
        }
        // Initialize views
        activityStatusTextView = findViewById(R.id.subtitle_activity);
        stepsTextView = findViewById(R.id.steps_count);
        walkingDistanceTextView = findViewById(R.id.walking_distance);
        speedTextView = findViewById(R.id.speed);
        startTrackingButton = findViewById(R.id.start_tracking_button);
        activityLogButton = findViewById(R.id.activity_log_btn);

        // Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_dashboard) {
                return true;
            } else if (item.getItemId() == R.id.navigation_compass) {
                startActivity(new Intent(Dashboard.this, CompassActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(Dashboard.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_settings) {
                startActivity(new Intent(Dashboard.this, SettingsActivity.class));
                return true;
            }
            return false;
        });

        // Initialize SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check permissions
        checkPermissions();

        // Handle start/stop tracking button
        startTrackingButton.setOnClickListener(v -> {
            if (isTracking) {
                stopTracking();
            } else {
                startTracking();
            }
        });

        activityLogButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ActivityLog.class);
            intent.putExtra("steps", stepsCount);
            intent.putExtra("distance", walkingDistance);
            intent.putExtra("speed", speed);
            startActivity(intent);
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Daily Reminder Channel";
            String description = "Channel for daily reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void checkPermissions() {
        // Check Activity Recognition permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_REQUEST_CODE);
        }

        // Check Location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    private void startTracking() {
        isTracking = true;
        startTrackingButton.setText("Stop Tracking");

        // Register step counter sensor listener
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }

        // Request location updates for speed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }

    private void stopTracking() {
        isTracking = false;
        startTrackingButton.setText("Start Tracking");

        // Unregister sensor and location listeners
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isTracking && event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepsCount = (int) event.values[0];
            walkingDistance = stepsCount * 0.0008; // Approximation: 0.8 meters per step
            stepsTextView.setText(stepsCount + " steps");
            walkingDistanceTextView.setText(String.format("%.2f km", walkingDistance));

            // Check for goal notification
            checkNotifications();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this implementation
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (isTracking) {
            speed = location.getSpeed() * 3.6; // Convert from m/s to km/h
            speedTextView.setText(String.format("%.2f km/h", speed));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start location updates if permission granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            }
        }
    }

    private void checkNotifications() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean goalNotificationsEnabled = sharedPreferences.getBoolean("goal_notifications", false);

        // Goal notification condition
        if (goalNotificationsEnabled) {
            showGoalNotification();
        }

    }

    private void showGoalNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, Dashboard.class); // Open the Dashboard activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Goal Reached!")
                .setContentText("You have reached your goal of 1000 steps today!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }

    private void scheduleDailyReminder(Context context) {
        Log.i("TAG", "scheduleDailyReminder: Scheduling reminder");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DailyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long interval = 10 * 1000; // 4 hours in milliseconds
        long triggerAtMillis = System.currentTimeMillis() + interval;

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    interval,
                    pendingIntent
            );
            Log.i("TAG", "scheduleDailyReminder: Reminder scheduled every 4 hours.");
        } else {
            Log.e("TAG", "AlarmManager is null. Reminder not scheduled.");
        }
    }

}
