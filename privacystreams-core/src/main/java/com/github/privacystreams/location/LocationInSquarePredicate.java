package com.github.privacystreams.location;

/**
 * Check whether a location is in a square region.
 */

class LocationInSquarePredicate extends LocationProcessor<Boolean> {
    private final double minLat;
    private final double minLng;
    private final double maxLat;
    private final double maxLng;

    LocationInSquarePredicate(String latLonField, double minLat, double minLng, double maxLat, double maxLng) {
        super(latLonField);
        this.minLat = minLat;
        this.minLng = minLng;
        this.maxLat = maxLat;
        this.maxLng = maxLng;
        this.addParameters(minLat, minLng, maxLat, maxLng);
    }

    @Override
    protected Boolean processLocation(LatLon latLon) {
        if (latLon == null) return null;
        double lat = latLon.getLatitude();
        double lng = latLon.getLongitude();
        return minLat <= lat && lat <= maxLat && minLng <= lng && lng <= maxLng;
    }
}
