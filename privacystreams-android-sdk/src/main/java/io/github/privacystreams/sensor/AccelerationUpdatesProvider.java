package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of accelerometer sensor updates.
 */
class AccelerationUpdatesProvider extends SensorUpdatesProvider {

    AccelerationUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_ACCELEROMETER, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new Acceleration(
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
    }

}
