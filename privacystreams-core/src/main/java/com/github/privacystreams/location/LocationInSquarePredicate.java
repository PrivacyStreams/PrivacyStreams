package com.github.privacystreams.location;

/**
 * Check whether a location is in a square region.
 */

class LocationInSquarePredicate extends LocationProcessor<Boolean> {
    private final double minLat;
    private final double minLng;
    private final double maxLat;
    private final double maxLng;

    LocationInSquarePredicate(String latLngField, double minLat, double minLng, double maxLat, double maxLng) {
        super(latLngField);
        this.minLat = minLat;
        this.minLng = minLng;
        this.maxLat = maxLat;
        this.maxLng = maxLng;
        this.addParameters(minLat, minLng, maxLat, maxLng);
    }

    @Override
    protected Boolean processLocation(LatLng latLng) {
        if (latLng == null) return null;
        double lat = latLng.getLatitude();
        double lng = latLng.getLongitude();
        return minLat <= lat && lat <= maxLat && minLng <= lng && lng <= maxLng;
    }
}
