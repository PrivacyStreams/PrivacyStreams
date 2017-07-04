package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Accelerometer.
 */
@PSItem
public class LinearAccelerometer extends Item {

    /**
     * Acceleration force along the x axis (excluding gravity).
     */
    @PSItemField(type = Float.class)
    public static final String X = "x";

    /**
     * Acceleration force along the y axis (excluding gravity).
     */
    @PSItemField(type = Float.class)
    public static final String Y = "y";

    /**
     * Acceleration force along the z axis (excluding gravity).
     */
    @PSItemField(type = Float.class)
    public static final String Z = "z";

    LinearAccelerometer(float x, float y, float z) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    /**
     * Provide a live stream of sensor readings from linear acceleration sensor.
     * @return the provider.
     */
    public static MStreamProvider asUpdates(int sensorDelay){
        return new AccelerometerUpdatesProvider(sensorDelay);
    }
}
