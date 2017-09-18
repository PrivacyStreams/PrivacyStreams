package io.github.privacystreams.location;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class LocationStay extends Item {

    @PSItemField(type = double.class)
    public static final String LATITUDE_AVERAGE = "latitude_average";

    @PSItemField(type = double.class)
    public static final String LONGITUDE_AVERAGE = "longitude_average";

    @PSItemField(type = long.class)
    public static final String START_TIMESTAMP = "start_timestamp";

    @PSItemField(type = long.class)
    public static final String END_TIMESTAMP = "end_timestamp";

    @PSItemField(type = String.class)
    public static final String ADDRESS = "address";

    public static PStreamProvider getLocationStays(int milliTime, String level, String api_key) {
        return new LocationStayUpdatesProvider(milliTime, level, api_key);
    }

}