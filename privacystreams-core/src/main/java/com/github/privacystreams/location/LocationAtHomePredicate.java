package com.github.privacystreams.location;

/**
 * A predicate that checks whether a location is at home.
 */
class LocationAtHomePredicate extends LocationProcessor<Boolean> {

    LocationAtHomePredicate(String latLngField) {
        super(latLngField);
    }

    @Override
    protected Boolean processLocation(LatLng latLng) {
        // TODO check whether the latitude and longitude is at home
        return null;
    }
}
