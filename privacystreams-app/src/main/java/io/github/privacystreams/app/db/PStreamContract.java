package io.github.privacystreams.app.db;

import android.provider.BaseColumns;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.location.Geolocation;

public final class PStreamContract {
    private PStreamContract() {}

    public static class BaseEntry implements BaseColumns {
        public static final String TIME_CREATED = Item.TIME_CREATED;
    }

    public static class GeolocationEntry extends BaseEntry {
        public static final String BEARING = Geolocation.BEARING;
        public static final String ACCURACY = Geolocation.ACCURACY;
        public static final String SPEED = Geolocation.SPEED;
        public static final String PROVIDER = Geolocation.PROVIDER;
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}
