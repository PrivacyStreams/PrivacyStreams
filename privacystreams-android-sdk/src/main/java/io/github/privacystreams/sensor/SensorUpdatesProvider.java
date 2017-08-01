package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.PStreamProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Provide a live stream of sensor updates.
 */
abstract class SensorUpdatesProvider extends PStreamProvider {

    private transient SensorEventListener sensorListener;
    private transient SensorManager sensorManager;
    private transient Sensor sensor;

    private final int sensorType;
    private final int sensorDelay;

    SensorUpdatesProvider(int sensorType, int sensorDelay) {
        this.sensorType = sensorType;
        this.sensorDelay = sensorDelay;
        this.addParameters(sensorType, sensorDelay);
    }

    protected abstract void handleSensorEvent(SensorEvent sensorEvent);

    protected void handleAccuracyChange(Sensor sensor, int accuracy) {
        // Do nothing.
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        sensorManager.unregisterListener(sensorListener);
    }

    @Override
    protected void provide() {
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        sensorListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                handleSensorEvent(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                handleAccuracyChange(sensor, accuracy);
            }
        };

        sensorManager.registerListener(sensorListener, sensor, sensorDelay);
    }

}


