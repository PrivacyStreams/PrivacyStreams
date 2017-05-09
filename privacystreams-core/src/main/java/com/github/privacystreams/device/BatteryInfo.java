package com.github.privacystreams.device;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A BatteryInfo item represents an event about the device.
 */
@PSItem
public class BatteryInfo extends Item {
    /**
     * The timestamp of when the state is requested.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The level of when the state is requested.
     */
    @PSItemField(type = Float.class)
    public static final String LEVEL = "level";

    BatteryInfo(long timestamp, float level){
        setFieldValue(TIMESTAMP, timestamp);
        setFieldValue(LEVEL, level);
    }

    /**
     * Provider an SStream of current battery info.
     * @return the provider
     */
    public static SStreamProvider asSnapshot() {
        return new BatteryInfoProvider();
    }
}
