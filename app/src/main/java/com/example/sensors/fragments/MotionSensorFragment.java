package com.example.sensors.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sensors.views.LineChartView;

public class MotionSensorFragment extends Fragment implements SensorEventListener {

    private static final String ARG_TYPE = "sensor_type";
    private static final String ARG_TITLE = "sensor_title";

    private SensorManager sensorManager;
    private Sensor sensor;

    private TextView xText, yText, zText, normText;
    private LineChartView chartView;

    private int sensorType;
    private String title;

    public static MotionSensorFragment create(int type, String title) {
        MotionSensorFragment fragment = new MotionSensorFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TYPE, type);
        bundle.putString(ARG_TITLE, title);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(android.view.LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sensorType = requireArguments().getInt(ARG_TYPE);
        title = requireArguments().getString(ARG_TITLE);

        sensorManager = (SensorManager)
                requireActivity().getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(sensorType);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        TextView titleText = new TextView(requireContext());
        titleText.setText(title);
        titleText.setTextSize(26);
        titleText.setTextColor(Color.rgb(15, 23, 42));
        titleText.setTypeface(null, Typeface.BOLD);

        TextView subtitleText = new TextView(requireContext());
        subtitleText.setText("Analyse des axes X, Y, Z en temps réel");
        subtitleText.setTextSize(15);
        subtitleText.setTextColor(Color.rgb(100, 116, 139));
        subtitleText.setPadding(0, 6, 0, 22);

        CardView axesCard = new CardView(requireContext());
        axesCard.setRadius(30);
        axesCard.setCardElevation(8);
        axesCard.setCardBackgroundColor(Color.WHITE);
        axesCard.setUseCompatPadding(true);

        LinearLayout axesLayout = new LinearLayout(requireContext());
        axesLayout.setOrientation(LinearLayout.VERTICAL);
        axesLayout.setPadding(28, 24, 28, 24);

        TextView cardTitle = new TextView(requireContext());
        cardTitle.setText("Mesures instantanées");
        cardTitle.setTextSize(18);
        cardTitle.setTextColor(Color.rgb(30, 41, 59));
        cardTitle.setTypeface(null, Typeface.BOLD);
        cardTitle.setPadding(0, 0, 0, 18);

        xText = createAxisText("X", "--");
        yText = createAxisText("Y", "--");
        zText = createAxisText("Z", "--");

        normText = new TextView(requireContext());
        normText.setText("Norme : --");
        normText.setTextSize(22);
        normText.setTextColor(Color.rgb(37, 99, 235));
        normText.setTypeface(null, Typeface.BOLD);
        normText.setPadding(0, 20, 0, 0);

        axesLayout.addView(cardTitle);
        axesLayout.addView(xText);
        axesLayout.addView(yText);
        axesLayout.addView(zText);
        axesLayout.addView(normText);

        axesCard.addView(axesLayout);

        CardView chartCard = new CardView(requireContext());
        chartCard.setRadius(30);
        chartCard.setCardElevation(8);
        chartCard.setCardBackgroundColor(Color.WHITE);
        chartCard.setUseCompatPadding(true);

        LinearLayout chartLayout = new LinearLayout(requireContext());
        chartLayout.setOrientation(LinearLayout.VERTICAL);
        chartLayout.setPadding(18, 18, 18, 18);

        TextView graphTitle = new TextView(requireContext());
        graphTitle.setText("Courbe de la norme");
        graphTitle.setTextSize(18);
        graphTitle.setTextColor(Color.rgb(30, 41, 59));
        graphTitle.setTypeface(null, Typeface.BOLD);
        graphTitle.setPadding(6, 0, 0, 12);

        chartView = new LineChartView(requireContext());
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                520
        ));

        chartLayout.addView(graphTitle);
        chartLayout.addView(chartView);
        chartCard.addView(chartLayout);

        root.addView(titleText);
        root.addView(subtitleText);
        root.addView(axesCard);
        root.addView(chartCard);

        return root;
    }

    private TextView createAxisText(String axis, String value) {
        TextView textView = new TextView(requireContext());
        textView.setText(axis + " : " + value);
        textView.setTextSize(18);
        textView.setTextColor(Color.rgb(71, 85, 105));
        textView.setPadding(0, 6, 0, 6);
        return textView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sensor != null) {
            sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        } else {
            normText.setText("Capteur indisponible");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

        xText.setText("X : " + String.format("%.2f", x));
        yText.setText("Y : " + String.format("%.2f", y));
        zText.setText("Z : " + String.format("%.2f", z));
        normText.setText("Norme : " + String.format("%.2f", magnitude));

        chartView.addValue(magnitude);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}