package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of gravity sensor updates.
 */
class GravityUpdatesProvider extends SensorUpdatesProvider {

    GravityUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_GRAVITY, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new Gravity(
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
    }

}
