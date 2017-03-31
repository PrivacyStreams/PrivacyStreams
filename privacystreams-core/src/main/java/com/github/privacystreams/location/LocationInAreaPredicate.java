package com.github.privacystreams.location;

/**
 * Check whether a location is at home.
 */

class LocationInAreaPredicate extends LocationProcessor<Boolean> {
    private final double center_latitude;
    private final double center_longitude;
    private final double radius;

    LocationInAreaPredicate(String latLngField, double center_latitude, double center_longitude,
                            double radius) {
        super(latLngField);
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.radius = radius;
        this.addParameters(center_latitude, center_longitude, radius);
    }

    @Override
    protected Boolean processLocation(LatLng latLng) {
        double latitude = latLng.getLatitude();
        double longitude = latLng.getLongitude();
        double latitude_delta = latitude - this.center_latitude;
        double longitude_delta = longitude - this.center_longitude;
        double distance = Math.sqrt(latitude_delta*latitude_delta + longitude_delta*longitude_delta);
        return distance <= this.radius;
    }
}
