package io.github.privacystreams.sensor;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Acceleration.
 */
@PSItem
public class Acceleration extends Item {

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

    Acceleration(float x, float y, float z) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    /**
     * Provide a live stream of sensor readings from accelerometer.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new AccelerationUpdatesProvider(sensorDelay);
    }
}
