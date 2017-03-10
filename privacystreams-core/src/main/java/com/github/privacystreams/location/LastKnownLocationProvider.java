package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.utils.permission.PermissionUtils;
import com.github.privacystreams.utils.time.Duration;

import java.util.HashSet;


/**
 * Created by yuanchun on 21/11/2016.
 * location asUpdates
 */

class LastKnownLocationProvider extends SingleItemStreamProvider {

    LastKnownLocationProvider() {
        // TODO add a parameter in order to only require one of COARSE_LOCATION and FINE_LOCATION
        this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
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
        if (location != null) this.output(new GeoLocation(location));
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
