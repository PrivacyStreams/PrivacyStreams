package com.github.privacystreams.location;

import com.github.privacystreams.utils.LocationUtils;

/**
 * Check whether a location is in a circular region.
 */

class LocationInCirclePredicate extends LocationProcessor<Boolean> {
    private final double centerLat;
    private final double centerLng;
    private final double radius;

    LocationInCirclePredicate(String latLngField, double centerLat, double centerLng, double radius) {
        super(latLngField);
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.radius = radius;
        this.addParameters(centerLat, centerLng, radius);
    }

    @Override
    protected Boolean processLocation(LatLng latLng) {
        LatLng centerLatLng = new LatLng(centerLat, centerLng);
        Double distance = LocationUtils.getDistanceBetween(centerLatLng, latLng);
        if (distance == null) return null;
        return distance <= this.radius;
    }
}
