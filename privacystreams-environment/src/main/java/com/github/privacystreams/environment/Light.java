package com.github.privacystreams.environment;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by fanglinchen on 3/5/17.
 */

public class Light extends Item {
    public static final String INTENSITY = "intensity";
    public static final String TIMESTAMP = "timestamp";

    public Light(float intensity,
                         long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    public static MultiItemStreamProvider asUpdates(){
        return new LightUpdatesProvider();
    }


}
