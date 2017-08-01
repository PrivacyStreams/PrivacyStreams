package io.github.privacystreams.device;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Process the location field in an item.
 */
abstract class WifiApProcessor<Tout> extends ItemOperator<Tout> {

    private final String wifiApField;

    WifiApProcessor(String wifiApField) {
        this.wifiApField = Assertions.notNull("wifiApField", wifiApField);
        this.addParameters(this.wifiApField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String wifiApFieldValue = input.getValueByField(this.wifiApField);
        return this.processWifiAp(wifiApFieldValue);
    }

    protected abstract Tout processWifiAp(String wifiApFieldValue);
}
