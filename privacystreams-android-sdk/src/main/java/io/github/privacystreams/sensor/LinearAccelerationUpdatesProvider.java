package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of linear acceleration sensor updates.
 */
class LinearAccelerationUpdatesProvider extends SensorUpdatesProvider {

    LinearAccelerationUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_LINEAR_ACCELERATION, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new LinearAcceleration(
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
    }

}
