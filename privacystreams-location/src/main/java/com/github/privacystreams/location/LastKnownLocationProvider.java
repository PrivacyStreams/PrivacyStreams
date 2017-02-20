package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionUtils;
import com.github.privacystreams.core.utils.time.Duration;

/**
 * Created by yuanchun on 21/11/2016.
 * location asUpdates
 */

public class LastKnownLocationProvider extends SingleItemStreamProvider {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public LastKnownLocationProvider() {
    }

    @Override
    protected void provide(SingleItemStream output) {
        boolean permissionGranted = PermissionUtils.requestPermissions(this.getContext(), REQUIRED_PERMISSIONS);
        if (permissionGranted)
            this.getLastKnownLocation(output);
        else
            Logging.warn("permission not granted: " + Arrays.asList(REQUIRED_PERMISSIONS));
    }

    private void getLastKnownLocation(SingleItemStream output) {
        Context context = this.getContext();
        PermissionUtils.requestPermissions(context, REQUIRED_PERMISSIONS);

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Logging.warn("Need location permission.");
            return;
        }
        Location gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location location = betterLocation(gpsLocation, networkLocation);
        if (location == null) return;
        output.write(new GeoLocation(location));
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        return parameters;
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
