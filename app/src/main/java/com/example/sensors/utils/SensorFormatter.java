package com.example.sensors.utils;

import android.hardware.Sensor;

public class SensorFormatter {

    public static String format(Sensor sensor) {

        return "ID : " + sensor.getId() + "\n"
                + "Nom : " + sensor.getName() + "\n"
                + "Fabricant : " + sensor.getVendor() + "\n"
                + "Version : " + sensor.getVersion() + "\n"
                + "Type : " + sensor.getStringType() + "\n"
                + "Int Type : " + sensor.getType() + "\n"
                + "Résolution : " + sensor.getResolution() + "\n"
                + "Consommation : " + sensor.getPower() + " mA\n"
                + "Maximum Range : " + sensor.getMaximumRange() + "\n"
                + "Min Delay : " + sensor.getMinDelay() + " µs\n";
    }
}