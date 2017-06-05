package com.github.privacystreams.device;

/**
 * A predicate that checks whether the connected ap is the home ap.
 */
class WifiApAtHomePredicate extends WifiApProcessor<Boolean> {

    WifiApAtHomePredicate(String connectedAp) {
        super(connectedAp);
    }

    @Override
    protected Boolean processWifiAp(String connectedWifiApSSID) {
        // TODO Problem set: check whether the current connectedWifiApSSID is equal to the stored home wifi ssid.
        return null;
    }
}
