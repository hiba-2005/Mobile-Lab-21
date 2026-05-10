package com.example.sensors;

import android.hardware.Sensor;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.sensors.fragments.*;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        openFragment(new SensorsListFragment());

        navigationView.setNavigationItemSelectedListener(item -> {
            handleMenu(item);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void handleMenu(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sensors) {
            openFragment(new SensorsListFragment());
        } else if (id == R.id.menu_temperature) {
            openFragment(SensorGraphFragment.create(
                    Sensor.TYPE_AMBIENT_TEMPERATURE,
                    "Température ambiante",
                    false));
        } else if (id == R.id.menu_humidity) {
            openFragment(SensorGraphFragment.create(
                    Sensor.TYPE_RELATIVE_HUMIDITY,
                    "Humidité relative",
                    false));
        } else if (id == R.id.menu_proximity) {
            openFragment(SensorGraphFragment.create(
                    Sensor.TYPE_PROXIMITY,
                    "Capteur de proximité",
                    false));
        } else if (id == R.id.menu_magnetic) {
            openFragment(SensorGraphFragment.create(
                    Sensor.TYPE_MAGNETIC_FIELD,
                    "Champ magnétique",
                    true));
        } else if (id == R.id.menu_accelerometer) {
            openFragment(MotionSensorFragment.create(
                    Sensor.TYPE_ACCELEROMETER,
                    "Accéléromètre"));
        } else if (id == R.id.menu_gravity) {
            openFragment(MotionSensorFragment.create(
                    Sensor.TYPE_GRAVITY,
                    "Gravité"));
        } else if (id == R.id.menu_gyroscope) {
            openFragment(MotionSensorFragment.create(
                    Sensor.TYPE_GYROSCOPE,
                    "Gyroscope"));
        } else if (id == R.id.menu_steps) {
            openFragment(new StepCounterFragment());
        } else if (id == R.id.menu_compass) {
            openFragment(new CompassFragment());
        } else if (id == R.id.menu_activity) {
            openFragment(new ActivityRecognitionFragment());
        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}