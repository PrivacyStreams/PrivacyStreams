package io.github.privacystreams.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * Provide a live stream of step counter updates.
 */
class StepCounterUpdatesProvider extends SensorUpdatesProvider {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    StepCounterUpdatesProvider(int sensorDelay) {
        super(Sensor.TYPE_STEP_COUNTER, sensorDelay);
    }

    @Override
    protected void handleSensorEvent(SensorEvent sensorEvent) {
        output(new StepCounter(sensorEvent.values[0]));
    }

}
