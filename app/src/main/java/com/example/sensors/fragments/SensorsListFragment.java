package com.example.sensors.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sensors.utils.SensorFormatter;

import java.util.List;

public class SensorsListFragment extends Fragment {

    private LinearLayout listLayout;
    private SensorManager sensorManager;

    @Nullable
    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        ScrollView scrollView = new ScrollView(requireContext());
        scrollView.setBackgroundColor(Color.rgb(248, 250, 252));

        listLayout = new LinearLayout(requireContext());
        listLayout.setOrientation(LinearLayout.VERTICAL);
        listLayout.setPadding(24, 24, 24, 24);

        scrollView.addView(listLayout);

        sensorManager = (SensorManager) requireActivity()
                .getSystemService(Context.SENSOR_SERVICE);

        displaySensors();

        return scrollView;
    }

    private void displaySensors() {

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        TextView title = new TextView(requireContext());
        title.setText("Capteurs disponibles");
        title.setTextSize(26);
        title.setTextColor(Color.rgb(15, 23, 42));
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText(sensors.size() + " capteurs détectés sur ce téléphone");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.rgb(100, 116, 139));
        subtitle.setPadding(0, 6, 0, 22);

        listLayout.addView(title);
        listLayout.addView(subtitle);

        for (Sensor sensor : sensors) {
            addSensorCard(sensor);
        }
    }

    private void addSensorCard(Sensor sensor) {

        CardView card = new CardView(requireContext());
        card.setRadius(28);
        card.setCardElevation(7);
        card.setCardBackgroundColor(Color.WHITE);
        card.setUseCompatPadding(true);

        LinearLayout content = new LinearLayout(requireContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(26, 24, 26, 24);

        TextView name = new TextView(requireContext());
        name.setText(sensor.getName());
        name.setTextSize(18);
        name.setTextColor(Color.rgb(30, 41, 59));
        name.setTypeface(null, Typeface.BOLD);

        TextView type = new TextView(requireContext());
        type.setText(sensor.getStringType());
        type.setTextSize(14);
        type.setTextColor(Color.rgb(37, 99, 235));
        type.setPadding(0, 4, 0, 14);

        TextView details = new TextView(requireContext());
        details.setText(SensorFormatter.format(sensor));
        details.setTextSize(14);
        details.setTextColor(Color.rgb(71, 85, 105));
        details.setLineSpacing(4, 1);

        content.addView(name);
        content.addView(type);
        content.addView(details);

        card.addView(content);

        listLayout.addView(card);
    }
}