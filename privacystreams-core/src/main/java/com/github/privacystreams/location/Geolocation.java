package com.github.privacystreams.location;

import android.location.Location;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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
     * The value is a LatLon instance.
     */
    @PSItemField(type = LatLon.class)
    public static final String LAT_LON = "lat_lon";

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
     * The bearing of the location data.
     * Bearing is the horizontal direction of travel of this device,
     * and is not related to the device orientation. It is guaranteed to
     * be in the range (0.0, 360.0] if the device has a bearing.
     * If this location does not have a bearing, then the bearing value will be0.0.
     */
    @PSItemField(type = Float.class)
    public static final String BEARING = "bearing";

    /** Country level. This level's accuracy is about 100,000 meters. */
    public static final String LEVEL_COUNTRY = "country";
    static final int ACCURACY_COUNTRY = 100000;
    /** City level. This level's accuracy is about 10,000 meters. */
    public static final String LEVEL_CITY = "city";
    static final int ACCURACY_CITY = 10000;
    /** Neighborhood level. This level's accuracy is about 1,000 meters. */
    public static final String LEVEL_NEIGHBORHOOD = "neighborhood";
    static final int ACCURACY_NEIGHBORHOOD = 1000;
    /** Building level. This level's accuracy is about 100 meters. */
    public static final String LEVEL_BUILDING = "building";
    static final int ACCURACY_BUILDING = 100;
    /** Exact level. This level's accuracy is about 10 meters. */
    public static final String LEVEL_EXACT = "exact";
    static final int ACCURACY_EXACT = 10;

    Geolocation(Location location) {
        this.setFieldValue(TIMESTAMP, location.getTime());
        this.setFieldValue(PROVIDER, location.getProvider());
//        List<Double> coordinates = new ArrayList<>();
//        coordinates.add(location.getLatitude());
//        coordinates.add(location.getLongitude());
//        coordinates.add(location.getAltitude());
//        this.setFieldValue(COORDINATES, coordinates);
        LatLon latLon = new LatLon(location.getLatitude(), location.getLongitude());
        this.setFieldValue(LAT_LON, latLon);
        this.setFieldValue(ACCURACY, location.getAccuracy());
        this.setFieldValue(SPEED, location.getSpeed());
        this.setFieldValue(BEARING, location.getBearing());
    }

    /**
     * Provide a live stream of Geolocation as the location updates.
     * This provider requires a location permission based on the location level.
     * If `level` is `Geolocation.LEVEL_EXACT`, this provider requires `android.permission.ACCESS_COARSE_LOCATION` permission.
     * If `level` is any other level, this provider requires `android.permission.ACCESS_FINE_LOCATION` permission.
     *
     * @param interval The interval between each two location updates.
     * @param level The location granularity level, could be
     *              `Geolocation.LEVEL_COUNTRY`, `Geolocation.LEVEL_CITY`, `Geolocation.LEVEL_NEIGHBORHOOD`,
     *              `Geolocation.LEVEL_BUILDING`, or `Geolocation.LEVEL_EXACT`.
     * @return the provider
     */
    // @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, conditional = true)
    public static MStreamProvider asUpdates(long interval, String level) {
        if (Globals.LocationConfig.useGoogleService)
            return new GoogleLocationUpdatesProvider(interval, level);
        else
            return new LocationUpdatesProvider(interval, level);
    }

    /**
     * Provide an SStream of a Geolocation item, as the last known location.
     * If `level` is `Geolocation.LEVEL_EXACT`, this provider requires `android.permission.ACCESS_COARSE_LOCATION` permission.
     * If `level` is any other level, this provider requires `android.permission.ACCESS_FINE_LOCATION` permission.
     *
     * @param level The location granularity level, could be
     *              `Geolocation.LEVEL_COUNTRY`, `Geolocation.LEVEL_CITY`, `Geolocation.LEVEL_NEIGHBORHOOD`,
     *              `Geolocation.LEVEL_BUILDING`, or `Geolocation.LEVEL_EXACT`.
     * @return the provider
     */
    public static SStreamProvider asLastKnown(String level) {
        if (Globals.LocationConfig.useGoogleService)
            return new GoogleLastLocationProvider(level);
        else
            return new LastKnownLocationProvider(level);
    }

    /**
     * Provide an SStream of a Geolocation item, as the current location.
     * If `level` is `Geolocation.LEVEL_EXACT`, this provider requires `android.permission.ACCESS_COARSE_LOCATION` permission.
     * If `level` is any other level, this provider requires `android.permission.ACCESS_FINE_LOCATION` permission.
     *
     * @param level The location granularity level, could be
     *              `Geolocation.LEVEL_COUNTRY`, `Geolocation.LEVEL_CITY`,
     *              `Geolocation.LEVEL_NEIGHBORHOOD`, `Geolocation.LEVEL_BUILDING`,
     *              or `Geolocation.LEVEL_EXACT`.
     * @return the provider
     */
    public static SStreamProvider asCurrent(String level) {
        if (Globals.LocationConfig.useGoogleService)
            return new GoogleCurrentLocationProvider(level);
        else
            return new CurrentLocationProvider(level);
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
