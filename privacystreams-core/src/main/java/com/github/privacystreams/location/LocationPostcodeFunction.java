package com.github.privacystreams.location;

/**
 * Get the postcode based on coordinates.
 */

class LocationPostcodeFunction extends LocationProcessor<String> {

    LocationPostcodeFunction(String latLonField) {
        super(latLonField);
    }

    @Override
    protected String processLocation(LatLon latLon) {
        // TODO get the postcode based on latitude and longitude
        return null;
    }

}
