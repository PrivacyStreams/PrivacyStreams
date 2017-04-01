package com.github.privacystreams.location;

import android.location.Location;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Compute the distance between two locations.
 */
class LocationDistanceCalculator extends ItemFunction<Double> {

    private final String latLngField1;
    private final String latLngField2;

    LocationDistanceCalculator(String latLngField1, String latLngField2) {
        this.latLngField1 = Assertions.notNull("latLngField1", latLngField1);
        this.latLngField2 = Assertions.notNull("latLngField2", latLngField2);
        this.addParameters(latLngField1, latLngField2);
    }

    @Override
    public Double apply(UQI uqi, Item input) {
        LatLng latLng1 = input.getValueByField(this.latLngField1);
        LatLng latLng2 = input.getValueByField(this.latLngField2);
        float[] result = new float[1];
        Location.distanceBetween(latLng1.getLatitude(), latLng1.getLongitude(),
                latLng2.getLatitude(), latLng2.getLongitude(), result);
        return (double) result[0];
    }
}
