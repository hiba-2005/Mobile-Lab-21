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

import java.util.LinkedList;
import java.util.Queue;

public class ActivityRecognitionFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView xText, yText, zText, movementText, activityText;

    private final float[] gravity = new float[3];
    private final Queue<Float> movements = new LinkedList<>();

    private static final int WINDOW = 25;
    private static final float ALPHA = 0.8f;

    @Nullable
    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        sensorManager = (SensorManager) requireActivity()
                .getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        TextView title = new TextView(requireContext());
        title.setText("Reconnaissance d’activité");
        title.setTextSize(25);
        title.setTextColor(Color.rgb(15, 23, 42));
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText("Détection simple du mouvement avec l’accéléromètre");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.rgb(100, 116, 139));
        subtitle.setPadding(0, 6, 0, 22);

        CardView card = new CardView(requireContext());
        card.setRadius(30);
        card.setCardElevation(8);
        card.setCardBackgroundColor(Color.WHITE);
        card.setUseCompatPadding(true);

        LinearLayout content = new LinearLayout(requireContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(28, 28, 28, 28);

        activityText = new TextView(requireContext());
        activityText.setText("Activité : --");
        activityText.setTextSize(24);
        activityText.setTextColor(Color.rgb(37, 99, 235));
        activityText.setTypeface(null, Typeface.BOLD);
        activityText.setPadding(0, 0, 0, 22);

        xText = createLine("X", "--");
        yText = createLine("Y", "--");
        zText = createLine("Z", "--");
        movementText = createLine("Mouvement", "--");

        content.addView(activityText);
        content.addView(xText);
        content.addView(yText);
        content.addView(zText);
        content.addView(movementText);

        card.addView(content);

        root.addView(title);
        root.addView(subtitle);
        root.addView(card);

        return root;
    }

    private TextView createLine(String label, String value) {
        TextView tv = new TextView(requireContext());
        tv.setText(label + " : " + value);
        tv.setTextSize(18);
        tv.setTextColor(Color.rgb(71, 85, 105));
        tv.setPadding(0, 7, 0, 7);
        return tv;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_GAME
            );
        } else {
            activityText.setText("Accéléromètre indisponible");
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

        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * x;
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * y;
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * z;

        float linearX = x - gravity[0];
        float linearY = y - gravity[1];
        float linearZ = z - gravity[2];

        float movement = (float) Math.sqrt(
                linearX * linearX + linearY * linearY + linearZ * linearZ
        );

        addMovement(movement);

        String activity = detectActivity(movement, z);

        xText.setText("X : " + String.format("%.2f", x));
        yText.setText("Y : " + String.format("%.2f", y));
        zText.setText("Z : " + String.format("%.2f", z));
        movementText.setText("Mouvement : " + String.format("%.2f", movement));
        activityText.setText("Activité : " + activity);
    }

    private void addMovement(float value) {
        if (movements.size() >= WINDOW) {
            movements.poll();
        }
        movements.add(value);
    }

    private String detectActivity(float movement, float z) {
        if (movement > 12) {
            return "Saut";
        }

        if (movement > 2) {
            return "Marche";
        }

        if (Math.abs(z) > 8) {
            return "Téléphone stable";
        }

        return "Position calme";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}