package com.github.privacystreams.location;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * Created by fanglinchen on 1/12/17.
 */

public class LocationStay extends Item{

    /**
     * The gps coordinate of the location stay.
     * The value is a list of double numbers, including latitude, longitude, and (optional) altitude.
     */
    @PSItemField(type = List.class)
    public static final String COORDINATE = "coordinate";

    /**
     * A list of addresses that are nearby the location stay.
     * The value is a list of addresses in string.
     */
    @PSItemField(type = List.class)
    public static final String ADDRESSES = "addresses";

    /**
     * A list of address types that describe the addresses.
     * The value is a list of addresses in string.
     */
    @PSItemField(type = List.class)
    public static final String TYPES = "types";

    /**
     * The timestamp of when the user arrives at this location.
     */
    @PSItemField(type = Long.class)
    public static final String START_TIMESTAMP = "start_timestamp";

    /**
     * The timestamp of when the user leaves at this location.
     */
    @PSItemField(type = Long.class)
    public static final String END_TIMESTAMP = "end_timestamp";

    LocationStay(List<Double> coordinates,
                 List<String> addresses,
                 List<String> types,
                 long startTimestamp,
                 long endTimestamp){

        this.setFieldValue(COORDINATE, coordinates);
        this.setFieldValue(ADDRESSES, addresses);
        this.setFieldValue(TYPES,types);
        this.setFieldValue(START_TIMESTAMP, startTimestamp);
        this.setFieldValue(END_TIMESTAMP, endTimestamp);
    }

    /**
     * Provide a live stream of location stays.
     * @return the provider function
     */
    public static Function<Void, MStream> asLocationStayUpdates(String provider,
                                                                float minDistance,
                                                                String developerKey){
            return new LocationStayUpdatesProvider(provider,minDistance,developerKey);
    }
}
