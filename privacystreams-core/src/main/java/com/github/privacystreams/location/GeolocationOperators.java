package com.github.privacystreams.location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access geolocation-related operators
 */
@PSOperatorWrapper
public class GeolocationOperators {

//    /**
//     * Check if the coordinates value of a field is a location at home.
//     *
//     * @param latLonField the coordinates field to check
//     * @return the predicate
//     */
//    public static Function<Item, Boolean> atHome(String latLonField) {
//        return new LocationAtHomePredicate(latLonField);
//    }

    /**
     * Check if the coordinates specified by a LatLon field is a location in an given circular region.
     *
     * @param latLonField the LatLon field to check
     * @param centerLat latitude of the center of the area
     * @param centerLng longitude of the center of the area
     * @param radius radius of the region, in meters
     * @return the function
     */
    public static Function<Item, Boolean> inCircle(String latLonField,
                                                   double centerLat, double centerLng, double radius) {
        return new LocationInCirclePredicate(latLonField, centerLat, centerLng, radius);
    }

    /**
     * Check if the coordinates specified by a LatLon field is a location in an given square region.
     *
     * @param latLonField the LatLon field to check
     * @param minLat the minimum latitude of the region
     * @param minLng the minimum longitude of the region
     * @param maxLat the maximum latitude of the region
     * @param maxLng the maximum longitude of the region
     * @return the function
     */
    public static Function<Item, Boolean> inSquare(String latLonField,
                                                   double minLat, double minLng, double maxLat, double maxLng) {
        return new LocationInSquarePredicate(latLonField, minLat, minLng, maxLat, maxLng);
    }

    /**
     * Distort the coordinates value of a field and return the distorted coordinates.
     * The distorted coordinates is an instance of `LatLon` class.
     *
     * @param latLonField the coordinates field to distort
     * @param radius the distance to distort, in meters
     * @return the function
     */
    public static Function<Item, LatLon> distort(String latLonField, double radius) {
        return new LocationDistorter(latLonField, radius);
    }

    /**
     * Get the distance between two locations (in meters).
     *
     * @param latLonField1 the first location
     * @param latLonField2 the second location
     * @return the function
     */
    public static Function<Item, Double> distanceBetween(String latLonField1, String latLonField2) {
        return new LocationDistanceCalculator(latLonField1, latLonField2);
    }

//    /**
//     * Get the postcode string based on the coordinates value of a field.
//     *
//     * @param latLonField the coordinates field
//     * @return the function
//     */
//    public static Function<Item, String> asPostcode(String latLonField) {
//        return new LocationPostcodeFunction(latLonField);
//    }
//
//    /**
//     * Get the geotag string based on the coordinates value of a field.
//     *
//     * @param latLonField the coordinates field
//     * @return the function
//     */
//    public static Function<Item, String> asGeotag(String latLonField) {
//        return new LocationGeoTagger(latLonField);
//    }
}
