package com.github.privacystreams.environment;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A LightEnv item represents the data read from light environment sensor.
 */
@PSItem
public class LightEnv extends Item {

    /**
     * The light intensity, in lumens.
     */
    @PSItemField(type = Float.class)
    public static final String INTENSITY = "intensity";

    /**
     * The timestamp of when the light sensor value is read.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    LightEnv(float intensity, long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    /**
     * Provide a live stream of LightEnv items that are read from the light sensor.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates(){
        return new LightUpdatesProvider();
    }
}