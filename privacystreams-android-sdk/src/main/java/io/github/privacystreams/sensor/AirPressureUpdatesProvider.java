package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of air pressure sensor updates.
 */
class AirPressureUpdatesProvider extends SensorUpdatesProvider {

    AirPressureUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_PRESSURE, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new AirPressure(sensorEvent.values[0]));
    }

}
