package com.github.privacystreams.location;

/**
 * Compute the blurred coordinates
 */

class LocationPostcodeFunction extends LocationProcessor<String> {

    LocationPostcodeFunction(String coordinatesField) {
        super(coordinatesField);
    }

    @Override
    protected String processLocation(double latitude, double longitude) {
        // TODO get the postcode based on latitude and longitude
        return null;
    }

}
