package com.github.privacystreams.environment;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A Light item represents the data read from light sensor.
 */
@PSItem
public class Light extends Item {

    /**
     * The light intensity, in ??.
     */
    @PSItemField(type = Float.class)
    public static final String INTENSITY = "intensity";

    /**
     * The timestamp of when the light sensor value is read.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    public Light(float intensity, long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static Function<Void, MultiItemStream> asUpdates(){
        return new LightUpdatesProvider();
    }


}
