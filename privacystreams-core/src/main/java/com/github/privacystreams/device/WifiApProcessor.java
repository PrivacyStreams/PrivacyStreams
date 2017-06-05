package com.github.privacystreams.device;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the location field in an item.
 */
abstract class WifiApProcessor< Tout> extends ItemFunction<Tout> {

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
