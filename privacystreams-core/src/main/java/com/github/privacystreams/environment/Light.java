package com.github.privacystreams.environment;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A Light item represents the data read from light sensor.
 */
@PSItem
public class Light extends Item {
    @PSItemField(name = "intensity", type = Float.class, description = "The light intensity, in ??.")
    public static final String INTENSITY = "intensity";

    @PSItemField(name = "timestamp", type = Long.class, description = "The timestamp of when the light sensor value is read.")
    public static final String TIMESTAMP = "timestamp";

    public Light(float intensity, long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new LightUpdatesProvider();
    }


}
