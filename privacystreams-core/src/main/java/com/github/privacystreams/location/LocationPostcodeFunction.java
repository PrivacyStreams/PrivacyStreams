package com.github.privacystreams.location;

/**
 * Compute the blurred coordinates
 */

class LocationPostcodeFunction extends LocationProcessor<String> {

    LocationPostcodeFunction(String latLngField) {
        super(latLngField);
    }

    @Override
    protected String processLocation(LatLng latLng) {
        // TODO get the postcode based on latitude and longitude
        return null;
    }

}
