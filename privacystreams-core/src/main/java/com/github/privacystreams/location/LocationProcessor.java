package com.github.privacystreams.location;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the location field in an item.
 */
abstract class LocationProcessor<Tout> extends ItemFunction<Tout> {

    private final String latLonField;

    LocationProcessor(String latLonField) {
        this.latLonField = Assertions.notNull("latLonField", latLonField);
        this.addParameters(this.latLonField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        LatLon latLon = input.getValueByField(this.latLonField);
        return this.processLocation(latLon);
    }

    protected abstract Tout processLocation(LatLon latLon);
}
