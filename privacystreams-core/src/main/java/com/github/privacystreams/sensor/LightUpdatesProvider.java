package com.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Provide a live stream of light sensor updates.
 */
class LightUpdatesProvider extends SensorUpdatesProvider {

    LightUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_LIGHT, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new LightSensor(sensorEvent.values[0]));
    }

}
