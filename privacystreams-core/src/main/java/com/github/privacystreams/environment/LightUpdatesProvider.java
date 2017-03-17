package com.github.privacystreams.environment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by fanglinchen on 3/5/17.
 */
class LightUpdatesProvider extends MultiItemStreamProvider {

    private transient MyLightListener lightListener;
    private transient SensorManager sensorManager;

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        sensorManager.unregisterListener(lightListener);
    }

    @Override
    protected void provide() {
        lightListener = new MyLightListener();
    }

    private final class MyLightListener implements SensorEventListener{

        private Sensor lightSensor;

        MyLightListener(){
            sensorManager = (SensorManager)getContext().getSystemService(SENSOR_SERVICE);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            sensorManager.registerListener(this,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            output(new Light(event.values[0], System.currentTimeMillis()));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}


