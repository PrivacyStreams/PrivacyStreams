package com.github.privacystreams.location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access location-related operators
 */
@PSOperatorWrapper
public class LocationOperators {

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
     * Check if the coordinates value of a field is a location in an given round area.
     *
     * @param latLngField the coordinates field to check
     * @param center_lat latitude of the center of the area
     * @param center_lng longitude of the center of the area
     * @param radius radius of the given area
     * @return the predicate
     */
    public static Function<Item, Boolean> inArea(String latLngField,
                                                 double center_lat, double center_lng, double radius) {
        return new LocationInAreaPredicate(latLngField, center_lat, center_lng, radius);
    }

    /**
     * Blur the coordinates value of a field and return the blurred coordinates.
     * The blurred coordinates is an instance of `LatLng` class.
     *
     * @param latLngField the coordinates field to blur
     * @param blurMeters the distance to blur, in meters
     * @return the function
     */
    public static Function<Item, LatLng> blur(String latLngField, double blurMeters) {
        return new LocationBlurFunction(latLngField, blurMeters);
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
