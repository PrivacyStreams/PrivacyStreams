package io.github.privacystreams.location;

/**
 * Get the postcode based on coordinates.
 */

class LocationPostcodeOperator extends LocationProcessor<String> {

    LocationPostcodeOperator(String latLonField) {
        super(latLonField);
    }

    @Override
    protected String processLocation(LatLon latLon) {
        // TODO get the postcode based on latitude and longitude
        return null;
    }

}
