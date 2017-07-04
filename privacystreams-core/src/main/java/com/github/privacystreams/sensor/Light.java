package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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
    public static MStreamProvider asUpdates(int sensorDelay){
        return new LightUpdatesProvider(sensorDelay);
    }
}
