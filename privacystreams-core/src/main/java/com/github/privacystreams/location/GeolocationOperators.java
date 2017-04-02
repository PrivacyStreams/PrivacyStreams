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
//     * @param latLngField the coordinates field to check
//     * @return the predicate
//     */
//    public static Function<Item, Boolean> atHome(String latLngField) {
//        return new LocationAtHomePredicate(latLngField);
//    }

    /**
     * Check if the coordinates specified by a LatLng field is a location in an given circular region.
     *
     * @param latLngField the LatLng field to check
     * @param centerLat latitude of the center of the area
     * @param centerLng longitude of the center of the area
     * @param radius radius of the region, in meters
     * @return the function
     */
    public static Function<Item, Boolean> inCircle(String latLngField,
                                                   double centerLat, double centerLng, double radius) {
        return new LocationInCirclePredicate(latLngField, centerLat, centerLng, radius);
    }

    /**
     * Check if the coordinates specified by a LatLng field is a location in an given square region.
     *
     * @param latLngField the LatLng field to check
     * @param minLat the minimum latitude of the region
     * @param minLng the minimum longitude of the region
     * @param maxLat the maximum latitude of the region
     * @param maxLng the maximum longitude of the region
     * @return the function
     */
    public static Function<Item, Boolean> inSquare(String latLngField,
                                                   double minLat, double minLng, double maxLat, double maxLng) {
        return new LocationInSquarePredicate(latLngField, minLat, minLng, maxLat, maxLng);
    }

    /**
     * Distort the coordinates value of a field and return the distorted coordinates.
     * The distorted coordinates is an instance of `LatLng` class.
     *
     * @param latLngField the coordinates field to distort
     * @param radius the distance to distort, in meters
     * @return the function
     */
    public static Function<Item, LatLng> distort(String latLngField, double radius) {
        return new LocationDistorter(latLngField, radius);
    }

    /**
     * Get the distance between two locations (in meters).
     *
     * @param latLngField1 the first location
     * @param latLngField2 the second location
     * @return the function
     */
    public static Function<Item, Double> distanceBetween(String latLngField1, String latLngField2) {
        return new LocationDistanceCalculator(latLngField1, latLngField2);
    }

//    /**
//     * Get the postcode string based on the coordinates value of a field.
//     *
//     * @param latLngField the coordinates field
//     * @return the function
//     */
//    public static Function<Item, String> asPostcode(String latLngField) {
//        return new LocationPostcodeFunction(latLngField);
//    }
//
//    /**
//     * Get the geotag string based on the coordinates value of a field.
//     *
//     * @param latLngField the coordinates field
//     * @return the function
//     */
//    public static Function<Item, String> asGeotag(String latLngField) {
//        return new LocationGeoTagger(latLngField);
//    }
}
