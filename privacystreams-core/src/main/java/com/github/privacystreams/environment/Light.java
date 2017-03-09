package com.github.privacystreams.environment;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.ItemField;

/**
 * A Light item represents the data read from light sensor.
 */

public class Light extends Item {
    @ItemField(name=INTENSITY, type = Float.class, description = "The light intensity, in .")
    public static final String INTENSITY = "intensity";

    @ItemField(name=TIMESTAMP, type = Long.class, description = "The timestamp of when the light sensor value is read.")
    public static final String TIMESTAMP = "timestamp";

    public Light(float intensity, long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new LightUpdatesProvider();
    }


}
