package com.github.privacystreams.location;

/**
 * A function that computes the geo tag based on coordinates
 */

class LocationGeoTagger extends LocationProcessor<String> {

    LocationGeoTagger(String latLonField) {
        super(latLonField);
    }

    @Override
    protected String processLocation(LatLon latLon) {
        // TODO get the geotag based on latitude and longitude
        return null;
    }
}
