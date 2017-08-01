package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of light sensor updates.
 */
class LightUpdatesProvider extends SensorUpdatesProvider {

    LightUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_LIGHT, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new Light(sensorEvent.values[0]));
    }

}
