package com.github.privacystreams.location;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Logging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Provide current location with Google API.
 */
class GoogleCurrentLocationProvider extends SStreamProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener  {

    private static final String LOG_TAG = "[GoogleCurrentLocationProvider]";

    private final String level;

    protected GoogleCurrentLocationProvider(String level) {
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

    // When location changes return the new location and stop listening to location updates.
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;
        this.output(new Geolocation(location));
        this.stopLocationUpdate();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        long interval = 100L;
        long fastInterval = interval / 2;
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastInterval);

        if (Geolocation.LEVEL_EXACT.equals(this.level))
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        else if (Geolocation.LEVEL_BUILDING.equals(this.level))
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        else
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, mLocationRequest, this);
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

    private void stopLocationUpdate() {
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        this.finish();
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        stopLocationUpdate();
    }
}
