package com.github.privacystreams.location;

import android.location.Location;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
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


    GeoLocation(Location location) {
        this.setFieldValue(TIMESTAMP, location.getTime());
        this.setFieldValue(PROVIDER, location.getProvider());
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(location.getLatitude());
        coordinates.add(location.getLongitude());
        coordinates.add(location.getAltitude());
        this.setFieldValue(COORDINATES, coordinates);
        this.setFieldValue(ACCURACY, location.getAccuracy());
        this.setFieldValue(SPEED,location.getSpeed());
    }


    public static MStreamProvider asUpdates(long interval, long fastInterval, int priority) {
        return new GoogleLocationProvider( interval, fastInterval, priority);
    }

    /**
     * Provide a GeoLocation item, which is the last known location.
     *
     * @return the stream provider
     */
    public static SStreamProvider asLastKnown() {
        return null;
    }

    /**
     * Provide a list of GeoLocation items, which are the location history of the device.
     *
     * @return the stream provider
     */
    public static MStreamProvider asHistory() {
        // TODO implement this
        return null;
    }
}
