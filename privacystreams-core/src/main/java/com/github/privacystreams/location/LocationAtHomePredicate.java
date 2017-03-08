package com.github.privacystreams.location;

/**
 * Created by yuanchun on 29/12/2016.
 * A predicate that checks whether a location is at home.
 */
class LocationAtHomePredicate extends LocationProcessor<Boolean> {

    LocationAtHomePredicate(String coordinatesField) {
        super(coordinatesField);
    }

    @Override
    protected Boolean processLocation(double latitude, double longitude) {
        // TODO check whether the latitude and longitude is at home
        return null;
    }
}
