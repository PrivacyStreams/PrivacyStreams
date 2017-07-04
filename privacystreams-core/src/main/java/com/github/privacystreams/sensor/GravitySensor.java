package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Gravity sensor.
 */
@PSItem
public class GravitySensor extends Item {

    /**
     * Force of gravity along the x axis.
     */
    @PSItemField(type = Float.class)
    public static final String X = "x";

    /**
     * Force of gravity along the y axis.
     */
    @PSItemField(type = Float.class)
    public static final String Y = "y";

    /**
     * Force of gravity along the z axis.
     */
    @PSItemField(type = Float.class)
    public static final String Z = "z";

    GravitySensor(float x, float y, float z) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
    }

    /**
     * Provide a live stream of sensor readings from gravity sensor.
     * @return the provider.
     */
    public static MStreamProvider asUpdates(int sensorDelay){
        return new GravityUpdatesProvider(sensorDelay);
    }
}
