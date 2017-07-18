package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.PStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Ambient relative humidity sensor.
 */
@PSItem
public class RelativeHumidity extends Item {

    /**
     * Ambient relative humidity. Unit: %.
     */
    @PSItemField(type = Float.class)
    public static final String HUMIDITY = "humidity";

    RelativeHumidity(float humidity) {
        this.setFieldValue(HUMIDITY, humidity);
    }

    /**
     * Provide a live stream of sensor readings from ambient relative humidity sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new RelativeHumidityUpdatesProvider(sensorDelay);
    }
}
