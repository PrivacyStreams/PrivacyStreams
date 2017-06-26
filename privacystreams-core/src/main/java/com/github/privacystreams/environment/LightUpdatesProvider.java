package com.github.privacystreams.environment;

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
class LightUpdatesProvider extends MStreamProvider {

    private transient SensorEventListener lightListener;
    private transient SensorManager sensorManager;
    private transient Sensor lightSensor;

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        sensorManager.unregisterListener(lightListener);
    }

    @Override
    protected void provide() {
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                output(new LightSensor(event.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

}


