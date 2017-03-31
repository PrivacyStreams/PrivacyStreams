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
 * An Geolocation item represents a geolocation value.
 */
@PSItem
public class Geolocation extends Item {
    /**
     * The timestamp of the location.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

//    /**
//     * The coordinates of the location.
//     * The value is a list of double numbers, including latitude, longitude, and (optional) altitude.
//     */
//    @PSItemField(type = List.class)
//    public static final String COORDINATES = "coordinates";

    /**
     * The coordinates of the location.
     * The value is a LatLng instance.
     */
    @PSItemField(type = LatLng.class)
    public static final String LAT_LNG = "lat_lng";

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

    /**
     * The level of the location data,
     * could be "country"/"city"/"neighbourhood"/"building"/"meter".
     */
    public static final String LEVEL = "level";

    /**
     * A set of location levels can be used in Geolocation APIs.
     */
    public static class Level {
        /**
         * Country level. This level's accuracy is about 100,000 meters.
         */
        public static final String COUNTRY = "country";
        /**
         * City level. This level's accuracy is about 10,000 meters.
         */
        public static final String CITY = "city";
        /**
         * Neighborhood level. This level's accuracy is about 1,000 meters.
         */
        public static final String NEIGHBORHOOD = "neighborhood";
        /**
         * Building level. This level's accuracy is about 100 meters.
         */
        public static final String BUILDING = "building";
        /**
         * Exact level. This level's accuracy is about 10 meters.
         */
        public static final String EXACT = "exact";
    }

    Geolocation(Location location) {
        this.setFieldValue(TIMESTAMP, location.getTime());
        this.setFieldValue(PROVIDER, location.getProvider());
//        List<Double> coordinates = new ArrayList<>();
//        coordinates.add(location.getLatitude());
//        coordinates.add(location.getLongitude());
//        coordinates.add(location.getAltitude());
//        this.setFieldValue(COORDINATES, coordinates);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.setFieldValue(LAT_LNG, latLng);
        this.setFieldValue(ACCURACY, location.getAccuracy());
        this.setFieldValue(SPEED,location.getSpeed());
    }


    public static MStreamProvider asUpdates(long interval, String level) {
        return new GoogleLocationProvider(interval, level);
    }

    /**
     * Provide a Geolocation item, which is the last known location.
     *
     * @return the stream provider
     */
    public static SStreamProvider asLastKnown(String level) {
        return new GoogleLastLocationProvider(level);
    }

//    /**
//     * Provide a list of Geolocation items, which are the location history of the device.
//     *
//     * @return the stream provider
//     */
//    public static MStreamProvider asHistory() {
//        return null;
//    }
}
