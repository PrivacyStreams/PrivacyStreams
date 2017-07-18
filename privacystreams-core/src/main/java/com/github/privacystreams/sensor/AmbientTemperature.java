package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.PStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Ambient air temperature sensor.
 */
@PSItem
public class AmbientTemperature extends Item {

    /**
     * Ambient air temperature. Unit: °C.
     */
    @PSItemField(type = Float.class)
    public static final String TEMPERATURE = "temperature";

    AmbientTemperature(float temperature) {
        this.setFieldValue(TEMPERATURE, temperature);
    }

    /**
     * Provide a live stream of sensor readings from air temperature sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new AmbientTemperatureUpdatesProvider(sensorDelay);
    }
}
