package com.github.privacystreams.location;

import java.util.List;

/**
 * Created by yuanchun on 29/12/2016.
 * A function that computes the blurred coordinates
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
