package com.github.privacystreams.location;

import com.github.privacystreams.utils.LocationUtils;

/**
 * Compute the distorted coordinates.
 */
class LocationDistorter extends LocationProcessor<LatLng> {
    private final double radius;

    LocationDistorter(String latLngField, double radius) {
        super(latLngField);
        this.radius = radius;
        this.addParameters(radius);
    }

    @Override
    protected LatLng processLocation(LatLng latLng) {
        return LocationUtils.distortLatLng(latLng, this.radius);
    }
}
