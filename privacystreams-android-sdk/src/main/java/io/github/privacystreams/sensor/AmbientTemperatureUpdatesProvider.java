package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of air temperature sensor updates.
 */
class AmbientTemperatureUpdatesProvider extends SensorUpdatesProvider {

    AmbientTemperatureUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_AMBIENT_TEMPERATURE, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new AmbientTemperature(sensorEvent.values[0]));
    }

}
