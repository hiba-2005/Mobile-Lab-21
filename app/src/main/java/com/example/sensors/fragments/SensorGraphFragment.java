package com.example.sensors.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sensors.views.LineChartView;

public class SensorGraphFragment extends Fragment implements SensorEventListener {

    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_MAGNITUDE = "magnitude";

    private SensorManager sensorManager;
    private Sensor sensor;

    private TextView valueText;
    private LineChartView chart;

    private int sensorType;
    private String screenTitle;
    private boolean useMagnitude;

    private final Handler simulator = new Handler(Looper.getMainLooper());
    private float time = 0;

    public static SensorGraphFragment create(int type, String title, boolean magnitude) {
        SensorGraphFragment fragment = new SensorGraphFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putString(KEY_TITLE, title);
        bundle.putBoolean(KEY_MAGNITUDE, magnitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(android.view.LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sensorType = requireArguments().getInt(KEY_TYPE);
        screenTitle = requireArguments().getString(KEY_TITLE);
        useMagnitude = requireArguments().getBoolean(KEY_MAGNITUDE);

        sensorManager = (SensorManager)
                requireActivity().getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(sensorType);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        TextView title = new TextView(requireContext());
        title.setText(screenTitle);
        title.setTextSize(26);
        title.setTextColor(Color.rgb(15, 23, 42));
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText("Lecture du capteur en temps réel");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.rgb(100, 116, 139));
        subtitle.setPadding(0, 6, 0, 24);

        CardView valueCard = new CardView(requireContext());
        valueCard.setRadius(28);
        valueCard.setCardElevation(8);
        valueCard.setCardBackgroundColor(Color.WHITE);
        valueCard.setUseCompatPadding(true);

        valueText = new TextView(requireContext());
        valueText.setText("Valeur actuelle : --");
        valueText.setTextSize(22);
        valueText.setTextColor(Color.rgb(37, 99, 235));
        valueText.setTypeface(null, android.graphics.Typeface.BOLD);
        valueText.setPadding(28, 28, 28, 28);

        valueCard.addView(valueText);

        CardView chartCard = new CardView(requireContext());
        chartCard.setRadius(28);
        chartCard.setCardElevation(8);
        chartCard.setCardBackgroundColor(Color.WHITE);
        chartCard.setUseCompatPadding(true);

        chart = new LineChartView(requireContext());
        chart.setPadding(10, 10, 10, 10);
        chart.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                600
        ));

        chartCard.addView(chart);

        root.addView(title);
        root.addView(subtitle);
        root.addView(valueCard);
        root.addView(chartCard);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            valueText.setText("Capteur indisponible - Simulation activée");
            startSimulation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        simulator.removeCallbacksAndMessages(null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value;

        if (useMagnitude && event.values.length >= 3) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            value = (float) Math.sqrt(x * x + y * y + z * z);
        } else {
            value = event.values[0];
        }

        updateScreen(value);
    }

    private void updateScreen(float value) {
        valueText.setText("Valeur actuelle\n" + String.format("%.2f", value));
        chart.addValue(value);
    }

    private void startSimulation() {
        simulator.postDelayed(new Runnable() {
            @Override
            public void run() {
                time++;

                float fakeValue;

                if (sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    fakeValue = 25f + (float) Math.sin(time / 5f) * 2f;
                } else if (sensorType == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    fakeValue = 60f + (float) Math.sin(time / 6f) * 10f;
                } else if (sensorType == Sensor.TYPE_PROXIMITY) {
                    fakeValue = time % 4 < 2 ? 0f : 5f;
                } else {
                    fakeValue = 40f + (float) Math.sin(time / 4f) * 8f;
                }

                updateScreen(fakeValue);
                simulator.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}