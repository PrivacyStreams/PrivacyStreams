package com.github.privacystreams.location;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Logging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * Provide a last known location with Google API.
 */
class GoogleLastLocationProvider extends SStreamProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "[GoogleLastLocationProvider]";

    private final String level;

    protected GoogleLastLocationProvider(String level) {
        this.level = Assertions.notNull("level", level);
        this.addParameters(level);
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private transient GoogleApiClient mGoogleApiClient = null;

    @Override
    protected void provide() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            this.output(new Geolocation(mLastLocation));
        }
        else {
            Logging.warn("Fail to get last known location. Can get current location instead.");
        }
        this.finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Logging.debug(LOG_TAG + "Connection lost. Cause: Network Lost.");
        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Logging.debug(LOG_TAG + "Connection lost. Cause: Service Disconnected");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logging.warn(LOG_TAG + "Not connected with GoogleApiClient");
        this.finish();
    }
}
