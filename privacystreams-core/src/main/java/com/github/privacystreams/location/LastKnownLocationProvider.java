package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Duration;
import com.github.privacystreams.utils.PermissionUtils;


/**
 * Provide a last known location.
 */

class LastKnownLocationProvider extends SStreamProvider {

    private static final String LOG_TAG = "[LastKnownLocationProvider]";

    private final String level;

    LastKnownLocationProvider(String level) {
        this.level = Assertions.notNull("level", level);

        this.addParameters(level);
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    protected void provide() {
        this.getLastKnownLocation();
    }

    private void getLastKnownLocation() {
        Context context = this.getContext();

        Location gpsLocation = null;
        Location networkLocation = null;

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (PermissionUtils.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
            gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (PermissionUtils.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION))
            networkLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location location = betterLocation(gpsLocation, networkLocation);
        if (location != null) this.output(new Geolocation(location));
        this.finish();
    }

    /**
     * Determines whether one Location reading is better
     *
     * @param location1  The new Location that you want to evaluate
     * @param location2 The last location fix, to which you want to compare the new one
     * @return better location
     */
    private Location betterLocation(Location location1, Location location2) {
        if (location1 == null && location2 == null) return null;

        if (location1 != null && location2 == null) {
            return location1;
        }

        if (location1 == null) {
            return location2;
        }

        long timeDelta = location1.getTime() - location2.getTime();
        boolean isSignificantlyNewer = timeDelta > Duration.minutes(10);
        boolean isSignificantlyOlder = timeDelta < -Duration.minutes(10);
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return location1;
        } else if (isSignificantlyOlder) {
            return location2;
        }

        if (location1.getAccuracy() < location2.getAccuracy()) {
            return location1;
        } else {
            return location2;
        }
    }
}
