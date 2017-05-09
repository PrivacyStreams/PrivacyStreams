package com.github.privacystreams.location;

import com.github.privacystreams.utils.LocationUtils;

/**
 * Check whether a location is in a circular region.
 */

class LocationInCirclePredicate extends LocationProcessor<Boolean> {
    private final double centerLat;
    private final double centerLng;
    private final double radius;

    LocationInCirclePredicate(String latLonField, double centerLat, double centerLng, double radius) {
        super(latLonField);
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.radius = radius;
        this.addParameters(centerLat, centerLng, radius);
    }

    @Override
    protected Boolean processLocation(LatLon latLon) {
        LatLon centerLatLon = new LatLon(centerLat, centerLng);
        Double distance = LocationUtils.getDistanceBetween(centerLatLon, latLon);
        if (distance == null) return null;
        return distance <= this.radius;
    }
}
