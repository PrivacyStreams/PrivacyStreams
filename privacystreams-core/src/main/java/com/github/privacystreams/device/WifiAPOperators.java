package com.github.privacystreams.device;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access geolocation-related operators
 */
@PSOperatorWrapper
public class WifiAPOperators {

    /**
     * Check if the currently connected Wifi is a location at home.
     *
     * @param  connectedAp the ssid for the connected wifi ap
     * @return the predicate
     */
    public static Function<Item, Boolean> atHome(String connectedAp) {
        return new WifiApAtHomePredicate(connectedAp);
    }
}
