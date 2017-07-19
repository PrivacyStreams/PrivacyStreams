package io.github.privacystreams.sensor;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Light environment sensor.
 */
@PSItem
public class Light extends Item {

    /**
     * The light illuminance. Unit: lx.
     */
    @PSItemField(type = Float.class)
    public static final String ILLUMINANCE = "illuminance";

    Light(float illuminance) {
        this.setFieldValue(ILLUMINANCE, illuminance);
    }

    /**
     * Provide a live stream of sensor readings from light sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new LightUpdatesProvider(sensorDelay);
    }
}
