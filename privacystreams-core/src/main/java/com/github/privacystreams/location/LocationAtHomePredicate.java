package com.github.privacystreams.location;

/**
 * A predicate that checks whether a location is at home.
 */
class LocationAtHomePredicate extends LocationProcessor<Boolean> {

    LocationAtHomePredicate(String latLonField) {
        super(latLonField);
    }

    @Override
    protected Boolean processLocation(LatLon latLon) {
        // TODO check whether the latitude and longitude is at home
        return null;
    }
}
