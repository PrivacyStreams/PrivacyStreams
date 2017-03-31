package com.github.privacystreams.location;

/**
 * A function that computes the geo tag based on coordinates
 */

class LocationGeoTagger extends LocationProcessor<String> {

    LocationGeoTagger(String latLngField) {
        super(latLngField);
    }

    @Override
    protected String processLocation(LatLng latLng) {
        // TODO get the geotag based on latitude and longitude
        return null;
    }
}
