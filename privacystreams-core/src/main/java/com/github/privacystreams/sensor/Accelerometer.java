package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Light environment sensor.
 */
@PSItem
public class Accelerometer extends Item {

    /**
     * Acceleration force along the x axis (including gravity).
     */
    @PSItemField(type = Float.class)
    public static final String X = "x";

    /**
     * Acceleration force along the y axis (including gravity).
     */
    @PSItemField(type = Float.class)
    public static final String Y = "y";

    /**
     * Acceleration force along the z axis (including gravity).
     */
    @PSItemField(type = Float.class)
    public static final String Z = "z";

    Accelerometer(float x, float y, float z) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    /**
     * Provide a live stream of LightSensor items that are read from the light sensor.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates(int sensorDelay){
        return new AccelerometerUpdatesProvider(sensorDelay);
    }
}
