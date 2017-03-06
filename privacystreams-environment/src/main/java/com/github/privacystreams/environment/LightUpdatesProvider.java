package com.github.privacystreams.environment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by fanglinchen on 3/5/17.
 */

public class LightUpdatesProvider extends MultiItemStreamProvider {

//    private Light data;
//    private long cTime;                                 // The current time
//    private float cIntensity;                           // The current light intensity
    private transient MyLightListener lightListener;    // The light listenor
    private SensorManager sm;
    @Override
    protected void provide() {
    sm = (SensorManager)getContext().getSystemService(SENSOR_SERVICE);
    lightListener = new MyLightListener();
    }

    private final class MyLightListener implements SensorEventListener{

        private Sensor lightSensor;                             // Set up the sensor and its manager ready for use
        MyLightListener(){                                  // The constructor for the light listener
            lightSensor=sm.getDefaultSensor(Sensor.TYPE_LIGHT);
            sm.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);//Normal delay
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
         //   Log.e("Light",String.valueOf(event.values[0]));
            LightUpdatesProvider.this.output(new Light(event.values[0],System.currentTimeMillis()));        //Output a new Light object
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}


