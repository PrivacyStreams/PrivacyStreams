package com.github.privacystreams.location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

import java.util.List;

/**
 * A helper class to access location-related operators
 */

public class LocationOperators {

    /**
     * A predicate that returns true if the coordinate is at home.
     * @param coordinatesField the coordinates field to check
     * @return the predicate
     */
    public static Function<Item, Boolean> atHome(String coordinatesField) {
        return new LocationAtHomePredicate(coordinatesField);
    }

    /**
     * A predicate that returns true if the coordinate is in an given round area.
     * @param coordinatesField the coordinates field to check
     * @param center_latitude latitude of the center of the area
     * @param center_longitude longtitude of the center of the area
     * @param radius radius of the given area
     * @return the predicate
     */
    public static Function<Item, Boolean> inArea(String coordinatesField,
                                                 double center_latitude, double center_longitude, double radius) {
        return new LocationInAreaPredicate(coordinatesField, center_latitude, center_longitude, radius);
    }

    /**
     * A function that blurs the coordinates of an item and return the blurred coordinates.
     * The blurred coordinates is a list of double, the first double element is the latitude,
     * the second double element is the longitude.
     * @param coordinatesField the coordinates field to blur
     * @param blurMeters the distance to blur, in meters
     * @return the function
     */
    public static Function<Item, List<Double>> blur(String coordinatesField, double blurMeters) {
        return new LocationBlurFunction(coordinatesField, blurMeters);
    }

    /**
     * A function that returns the postcode based on the coordinates
     * @param coordinatesField the coordinates field
     * @return the function
     */
    public static Function<Item, String> asPostcode(String coordinatesField) {
        return new LocationPostcodeFunction(coordinatesField);
    }

    /**
     * A function that returns the geotag based on the coordinates
     * @param coordinatesField the coordinates field
     * @return the function
     */
    public static Function<Item, String> asGeotag(String coordinatesField) {
        return new LocationGeoTagger(coordinatesField);
    }
}
