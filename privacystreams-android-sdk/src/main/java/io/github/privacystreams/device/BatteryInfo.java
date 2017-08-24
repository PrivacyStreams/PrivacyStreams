package io.github.privacystreams.device;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

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

    BatteryInfo(long timestamp, float level) {
        setFieldValue(TIMESTAMP, timestamp);
        setFieldValue(LEVEL, level);
    }

    /**
     * Provide current battery info.
     *
     * @return the provider
     */
    public static PStreamProvider asSnapshot() {
        return new BatteryInfoProvider();
    }
}
