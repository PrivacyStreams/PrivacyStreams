package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Light environment sensor.
 */
@PSItem
public class LightSensor extends Item {

    /**
     * The light intensity, in lumen.
     */
    @PSItemField(type = Float.class)
    public static final String INTENSITY = "intensity";

    LightSensor(float intensity) {
        this.setFieldValue(INTENSITY, intensity);
    }

    /**
     * Provide a live stream of LightSensor items that are read from the light sensor.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates(int sensorDelay){
        return new LightUpdatesProvider(sensorDelay);
    }
}
