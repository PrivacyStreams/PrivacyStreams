package io.github.privacystreams.location;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * Created by xiaobing1117 on 2017/8/30.
 */

public class LocationStay extends Item{

    @PSItemField(type = double.class)
    public static final String X = "x";

    @PSItemField(type = double.class)
    public static final String Y = "y";

    @PSItemField(type = long.class)
    public static final String START_TIMESTAMP = "start_timestamp";

    @PSItemField(type = long.class)
    public static final String END_TIMESTAMP = "end_timestamp";

    @PSItemField(type = String.class)
    public static final String ADDRESS = "address";

    public static PStreamProvider getLocationStays(int milliTime, String level, String api_key){
        return new GoogleLocationUpdatesProvider(milliTime,level,api_key);
    }

}