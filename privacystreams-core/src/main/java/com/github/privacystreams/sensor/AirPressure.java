package com.github.privacystreams.sensor;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.PStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Air pressure environment sensor.
 */
@PSItem
public class AirPressure extends Item {

    /**
     * Ambient air pressure. Unit: hPa or mbar.
     */
    @PSItemField(type = Float.class)
    public static final String PRESSURE = "pressure";

    AirPressure(float pressure) {
        this.setFieldValue(PRESSURE, pressure);
    }

    /**
     * Provide a live stream of sensor readings from air pressure sensor.
     * @return the provider.
     */
    public static PStreamProvider asUpdates(int sensorDelay){
        return new AirPressureUpdatesProvider(sensorDelay);
    }
}
