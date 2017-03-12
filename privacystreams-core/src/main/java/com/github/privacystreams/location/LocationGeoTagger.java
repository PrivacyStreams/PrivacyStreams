package com.github.privacystreams.location;

/**
 * Created by yuanchun on 29/12/2016.
 * A function that computes the geotag based on coordinates
 */

class LocationGeoTagger extends LocationProcessor<String> {

    LocationGeoTagger(String coordinatesField) {
        super(coordinatesField);
    }

    @Override
    protected String processLocation(double latitude, double longitude) {
        // TODO get the geotag based on latitude and longitude
        return null;
    }
}
