package io.github.privacystreams.location;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Process the location field in an item.
 */
abstract class LocationProcessor<Tout> extends ItemOperator<Tout> {

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
