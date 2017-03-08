package com.github.privacystreams.location;

import android.location.Location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 07/12/2016.
 * An GeoLocation SingleItemStream represents a geolocation value
 */

public class GeoLocation extends Item {
    // type: Long
    public static final String TIMESTAMP = "timestamp";

    // type: List<Double>, the elements are latitude, longitude, altitude
    public static final String COORDINATES = "coordinates";

    // type: Double
    public static final String SPEED = "speed";

    // type: String, the provider of the location data, e.g. "gps", "network"
    public static final String PROVIDER = "provider";

    // type: Double, the accuracy of the location data, in meters
    public static final String ACCURACY = "accuracy";

    GeoLocation(long time, String provider, double latitude, double longitude,
                       double altitude, float accuracy, float speed) {
        this.setFieldValue(TIMESTAMP, time);
        this.setFieldValue(PROVIDER, provider);
        this.setFieldValue(ACCURACY, accuracy);
        this.setFieldValue(SPEED, speed);
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(latitude);
        coordinates.add(longitude);
        coordinates.add(altitude);
        this.setFieldValue(COORDINATES, coordinates);
    }

    GeoLocation(Location location) {
        this(
            location.getTime(),
            location.getProvider(),
            location.getLatitude(),
            location.getLongitude(),
            location.getAltitude(),
            location.getAccuracy(),
            location.getSpeed());

    }

    /**
     * Get a provider that provides a live stream of updated geolocation continuously
     * @return the stream provider
     */
    public static MultiItemStreamProvider asUpdates(String provider, long minTime, float minDistance) {
        return new LocationUpdatesProvider(provider, minTime, minDistance);
    }

    /**
     * Get a provider that provides a item of asLastKnown geolocation
     * @return the stream provider
     */
    public static SingleItemStreamProvider asLastKnown() {
        return null;
    }

    /**
     * Get a provider that provides a stream of historic geolocation
     * @return the stream provider
     */
    public static MultiItemStreamProvider asHistory() {
        // TODO implement this
        return null;
    }


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
