package io.github.privacystreams.sensor;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Rotation vector sensor.
 */
@PSItem
public class RotationVector extends Item {

    /**
     * Rotation vector component along the x axis (x * sin(θ/2)).
     */
    @PSItemField(type = Float.class)
    public static final String X = "x";

    /**
     * Rotation vector component along the y axis (y * sin(θ/2)).
     */
    @PSItemField(type = Float.class)
    public static final String Y = "y";

    /**
     * Rotation vector component along the z axis (z * sin(θ/2)).
     */
    @PSItemField(type = Float.class)
    public static final String Z = "z";

    /**
     * (Optional) Scalar component of the rotation vector ((cos(θ/2)).
     */
    @PSItemField(type = Float.class)
    public static final String SCALAR = "scalar";

    RotationVector(float x, float y, float z, float scalar) {
        this.setFieldValue(X, x);
        this.setFieldValue(Y, y);
        this.setFieldValue(Z, z);
        this.setFieldValue(SCALAR, scalar);
    }

    /**
     * Provide a live stream of sensor readings from rotation vector sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new RotationVectorUpdatesProvider(sensorDelay);
    }
}
