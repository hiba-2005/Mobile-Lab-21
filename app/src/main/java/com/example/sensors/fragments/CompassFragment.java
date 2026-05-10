package com.example.sensors.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class CompassFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, magneticSensor;

    private TextView degreeText, directionText, infoText;

    private final float[] gravity = new float[3];
    private final float[] magnetic = new float[3];

    private boolean gravityReady = false;
    private boolean magneticReady = false;

    @Nullable
    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        sensorManager = (SensorManager) requireActivity()
                .getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        TextView title = new TextView(requireContext());
        title.setText("Boussole numérique");
        title.setTextSize(26);
        title.setTextColor(Color.rgb(15, 23, 42));
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText("Orientation du téléphone avec accéléromètre et magnétomètre");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.rgb(100, 116, 139));
        subtitle.setPadding(0, 6, 0, 24);

        CardView card = new CardView(requireContext());
        card.setRadius(32);
        card.setCardElevation(8);
        card.setCardBackgroundColor(Color.WHITE);
        card.setUseCompatPadding(true);

        LinearLayout content = new LinearLayout(requireContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(28, 32, 28, 32);
        content.setGravity(android.view.Gravity.CENTER_HORIZONTAL);

        TextView icon = new TextView(requireContext());
        icon.setText("🧭");
        icon.setTextSize(60);
        icon.setGravity(android.view.Gravity.CENTER);
        icon.setPadding(0, 0, 0, 12);

        degreeText = new TextView(requireContext());
        degreeText.setText("--°");
        degreeText.setTextSize(42);
        degreeText.setTextColor(Color.rgb(37, 99, 235));
        degreeText.setTypeface(null, Typeface.BOLD);
        degreeText.setGravity(android.view.Gravity.CENTER);

        directionText = new TextView(requireContext());
        directionText.setText("Direction inconnue");
        directionText.setTextSize(24);
        directionText.setTextColor(Color.rgb(15, 23, 42));
        directionText.setTypeface(null, Typeface.BOLD);
        directionText.setGravity(android.view.Gravity.CENTER);
        directionText.setPadding(0, 8, 0, 16);

        infoText = new TextView(requireContext());
        infoText.setText("Tournez doucement le téléphone pour changer la direction.");
        infoText.setTextSize(15);
        infoText.setTextColor(Color.rgb(100, 116, 139));
        infoText.setGravity(android.view.Gravity.CENTER);

        content.addView(icon);
        content.addView(degreeText);
        content.addView(directionText);
        content.addView(infoText);

        card.addView(content);

        root.addView(title);
        root.addView(subtitle);
        root.addView(card);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        if (magneticSensor != null) {
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
        }

        if (accelerometer == null || magneticSensor == null) {
            directionText.setText("Capteur manquant");
            infoText.setText("La boussole nécessite l’accéléromètre et le magnétomètre.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravity, 0, 3);
            gravityReady = true;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetic, 0, 3);
            magneticReady = true;
        }

        if (gravityReady && magneticReady) {
            float[] rotation = new float[9];
            float[] orientation = new float[3];

            boolean success = SensorManager.getRotationMatrix(
                    rotation,
                    null,
                    gravity,
                    magnetic
            );

            if (success) {
                SensorManager.getOrientation(rotation, orientation);

                float azimuth = (float) Math.toDegrees(orientation[0]);

                if (azimuth < 0) {
                    azimuth += 360;
                }

                degreeText.setText(String.format("%.1f°", azimuth));
                directionText.setText(getDirectionName(azimuth));
            }
        }
    }

    private String getDirectionName(float degree) {
        if (degree >= 337.5 || degree < 22.5) {
            return "Nord";
        } else if (degree < 67.5) {
            return "Nord-Est";
        } else if (degree < 112.5) {
            return "Est";
        } else if (degree < 157.5) {
            return "Sud-Est";
        } else if (degree < 202.5) {
            return "Sud";
        } else if (degree < 247.5) {
            return "Sud-Ouest";
        } else if (degree < 292.5) {
            return "Ouest";
        } else {
            return "Nord-Ouest";
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}