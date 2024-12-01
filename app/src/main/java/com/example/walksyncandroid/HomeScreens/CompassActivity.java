package com.example.walksyncandroid.HomeScreens;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.example.walksyncandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private float[] gravity, geomagnetic;
    private TextView compassDegree, latitudeText, longitudeText;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compassDegree = findViewById(R.id.compass_degree);
        latitudeText = findViewById(R.id.latitude_text);
        longitudeText = findViewById(R.id.longitude_text);

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Initialize location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Register bottom navigation listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_compass) {
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                startActivity(new Intent(CompassActivity.this, Dashboard.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(CompassActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_settings) {
                startActivity(new Intent(CompassActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        // Request location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                // Convert radians to degrees
                float azimuth = (float) Math.toDegrees(orientation[0]);
                if (azimuth < 0) azimuth += 360;

                // Update compass degree
                compassDegree.setText(String.format("%.0f° %s", azimuth, getDirection(azimuth)));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update latitude and longitude
        latitudeText.setText(String.format("Latitude: %.5f", location.getLatitude()));
        longitudeText.setText(String.format("Longitude: %.5f", location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Deprecated but needed for older APIs
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // Not used
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // Not used
    }

    private String getDirection(float azimuth) {
        if (azimuth >= 337.5 || azimuth < 22.5) return "N";
        if (azimuth >= 22.5 && azimuth < 67.5) return "NE";
        if (azimuth >= 67.5 && azimuth < 112.5) return "E";
        if (azimuth >= 112.5 && azimuth < 157.5) return "SE";
        if (azimuth >= 157.5 && azimuth < 202.5) return "S";
        if (azimuth >= 202.5 && azimuth < 247.5) return "SW";
        if (azimuth >= 247.5 && azimuth < 292.5) return "W";
        if (azimuth >= 292.5 && azimuth < 337.5) return "NW";
        return "";
    }
}
