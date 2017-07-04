package com.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of accelerometer sensor updates.
 */
class AccelerometerUpdatesProvider extends SensorUpdatesProvider {

    AccelerometerUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_ACCELEROMETER, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new Accelerometer(
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
    }

}
