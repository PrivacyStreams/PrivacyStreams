package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Provide a live stream of ambient relative humidity sensor updates.
 */
class RelativeHumidityUpdatesProvider extends SensorUpdatesProvider {

    RelativeHumidityUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_RELATIVE_HUMIDITY, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new RelativeHumidity(sensorEvent.values[0]));
    }

}
