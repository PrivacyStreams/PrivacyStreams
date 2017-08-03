package io.github.privacystreams.app;

import io.github.privacystreams.location.Geolocation;

public class Config {
    public static long COLLECT_LOCATION_INTERVAL = 1000 * 10;
    public static String COLLECT_LOCATION_LEVEL = Geolocation.LEVEL_EXACT;
}
