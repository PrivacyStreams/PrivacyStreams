package com.github.privacystreams.location;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

import java.util.List;

/**
 * Process the location field in an item.
 */
abstract class LocationProcessor<Tout> extends ItemFunction<Tout> {

    private final String latLngField;

    LocationProcessor(String latLngField) {
        this.latLngField = Assertions.notNull("latLngField", latLngField);
        this.addParameters(this.latLngField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        LatLng latLng = input.getValueByField(this.latLngField);
        return this.processLocation(latLng);
    }

    protected abstract Tout processLocation(LatLng latLng);
}
