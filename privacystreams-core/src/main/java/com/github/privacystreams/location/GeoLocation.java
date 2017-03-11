package com.github.privacystreams.location;

import android.location.Location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.ArrayList;
import java.util.List;

/**
 * An GeoLocation item represents a geolocation value.
 */
@PSItem
public class GeoLocation extends Item {

    /**
     * The timestamp of the location.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The coordinates of the location.
     * The value is a list of double numbers, including latitude, longitude, and (optional) altitude.
     */
    @PSItemField(type = List.class)
    public static final String COORDINATES = "coordinates";

    /**
     * The speed at the location, in meters/second.
     */
    @PSItemField(type = Float.class)
    public static final String SPEED = "speed";

    /**
     * The provider of the location data, e.g., "gps" or "network".
     */
    @PSItemField(type = String.class)
    public static final String PROVIDER = "provider";

    /**
     * The accuracy of the location data, in meters.
     */
    @PSItemField(type = Float.class)
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
    public static Function<Void, MultiItemStream> asUpdates(String provider, long minTime, float minDistance) {
        return new LocationUpdatesProvider(provider, minTime, minDistance);
    }

    /**
     * Get a provider that provides a item of asLastKnown geolocation
     * @return the stream provider
     */
    public static Function<Void, SingleItemStream> asLastKnown() {
        return null;
    }

    /**
     * Get a provider that provides a stream of historic geolocation
     * @return the stream provider
     */
    public static Function<Void, MultiItemStream> asHistory() {
        // TODO implement this
        return null;
    }

}
