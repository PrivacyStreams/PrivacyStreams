package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of gyroscope updates.
 */
class GyroscopeUpdatesProvider extends SensorUpdatesProvider {

    GyroscopeUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_GYROSCOPE, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new Gyroscope(
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
    }

}
