package io.github.privacystreams.sensor;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Linear acceleration.
 */
@PSItem
public class LinearAcceleration extends Item {

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

    LinearAcceleration(float x, float y, float z) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    /**
     * Provide a live stream of sensor readings from linear acceleration sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new LinearAccelerationUpdatesProvider(sensorDelay);
    }
}
