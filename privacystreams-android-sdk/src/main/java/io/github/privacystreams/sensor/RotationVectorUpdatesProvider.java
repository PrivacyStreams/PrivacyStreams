package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of rotation vector sensor updates.
 */
class RotationVectorUpdatesProvider extends SensorUpdatesProvider {

    RotationVectorUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_ROTATION_VECTOR, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new RotationVector(sensorEvent.values[0], sensorEvent.values[1],
                sensorEvent.values[2], sensorEvent.values[3]));
    }

}
