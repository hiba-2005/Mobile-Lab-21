package com.example.sensors.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private TextView totalStepsText, sessionStepsText, statusText;

    private float firstValue = -1;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        if (granted) {
                            startCounter();
                        } else {
                            statusText.setText("Permission refusée");
                        }
                    });

    @Nullable
    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        sensorManager = (SensorManager) requireActivity()
                .getSystemService(Context.SENSOR_SERVICE);

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        TextView title = new TextView(requireContext());
        title.setText("Compteur de pas");
        title.setTextSize(26);
        title.setTextColor(Color.rgb(15, 23, 42));
        title.setTypeface(null, Typeface.BOLD);

        TextView subtitle = new TextView(requireContext());
        subtitle.setText("Suivi des pas depuis le démarrage et la session actuelle");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.rgb(100, 116, 139));
        subtitle.setPadding(0, 6, 0, 24);

        CardView totalCard = createCard();
        totalStepsText = createBigText("0");
        TextView totalLabel = createLabel("Pas depuis le démarrage");

        LinearLayout totalContent = createCardContent();
        totalContent.addView(totalLabel);
        totalContent.addView(totalStepsText);
        totalCard.addView(totalContent);

        CardView sessionCard = createCard();
        sessionStepsText = createBigText("0");
        TextView sessionLabel = createLabel("Pas de la session");

        LinearLayout sessionContent = createCardContent();
        sessionContent.addView(sessionLabel);
        sessionContent.addView(sessionStepsText);
        sessionCard.addView(sessionContent);

        statusText = new TextView(requireContext());
        statusText.setText("Marchez avec le téléphone pour tester le capteur.");
        statusText.setTextSize(15);
        statusText.setTextColor(Color.rgb(100, 116, 139));
        statusText.setPadding(4, 18, 4, 0);

        root.addView(title);
        root.addView(subtitle);
        root.addView(totalCard);
        root.addView(sessionCard);
        root.addView(statusText);

        return root;
    }

    private CardView createCard() {
        CardView card = new CardView(requireContext());
        card.setRadius(30);
        card.setCardElevation(8);
        card.setCardBackgroundColor(Color.WHITE);
        card.setUseCompatPadding(true);
        return card;
    }

    private LinearLayout createCardContent() {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 28, 28, 28);
        return layout;
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(requireContext());
        label.setText(text);
        label.setTextSize(16);
        label.setTextColor(Color.rgb(100, 116, 139));
        return label;
    }

    private TextView createBigText(String text) {
        TextView big = new TextView(requireContext());
        big.setText(text);
        big.setTextSize(40);
        big.setTextColor(Color.rgb(37, 99, 235));
        big.setTypeface(null, Typeface.BOLD);
        big.setPadding(0, 8, 0, 0);
        return big;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (stepSensor == null) {
            statusText.setText("Capteur de pas indisponible.");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {

            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            startCounter();
        }
    }

    private void startCounter() {
        sensorManager.registerListener(
                this,
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        statusText.setText("Capteur actif");
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float totalSteps = event.values[0];

        if (firstValue < 0) {
            firstValue = totalSteps;
        }

        int sessionSteps = (int) (totalSteps - firstValue);

        totalStepsText.setText(String.valueOf((int) totalSteps));
        sessionStepsText.setText(String.valueOf(sessionSteps));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}