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

    /**
     * Check if the coordinates value of a field is a location at home.
     *
     * @param coordinatesField the coordinates field to check
     * @return the predicate
     */
    public static Function<Item, Boolean> atHome(String coordinatesField) {
        return new LocationAtHomePredicate(coordinatesField);
    }

    /**
     * Check if the coordinates value of a field is a location in an given round area.
     *
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
     * Blur the coordinates value of a field and return the blurred coordinates.
     * The blurred coordinates is a list of double, the first double element is the latitude,
     * the second double element is the longitude.
     *
     * @param coordinatesField the coordinates field to blur
     * @param blurMeters the distance to blur, in meters
     * @return the function
     */
    public static Function<Item, List<Double>> blur(String coordinatesField, double blurMeters) {
        return new LocationBlurFunction(coordinatesField, blurMeters);
    }

    /**
     * Get the postcode string based on the coordinates value of a field.
     *
     * @param coordinatesField the coordinates field
     * @return the function
     */
    public static Function<Item, String> asPostcode(String coordinatesField) {
        return new LocationPostcodeFunction(coordinatesField);
    }

    /**
     * Get the geotag string based on the coordinates value of a field.
     *
     * @param coordinatesField the coordinates field
     * @return the function
     */
    public static Function<Item, String> asGeotag(String coordinatesField) {
        return new LocationGeoTagger(coordinatesField);
    }
}
