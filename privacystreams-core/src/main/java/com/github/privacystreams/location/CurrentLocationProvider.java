package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.Assertions;

/**
 * Provide current location with Android standard APIs.
 */

final class CurrentLocationProvider extends SStreamProvider {

    private final String level;

    CurrentLocationProvider(String level) {
        this.level = Assertions.notNull("level", level);

        this.addParameters(level);
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private transient LocationManager locationManager;
    private transient LocationListener locationListener;

    @Override
    protected void provide() {
        Looper.prepare();
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        long minTime = 0;
        float minDistance = 0;
        String provider;
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            provider = LocationManager.GPS_PROVIDER;
        }
        else {
            provider = LocationManager.NETWORK_PROVIDER;
        }
        locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
        Looper.loop();
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        this.stopLocationUpdate();
    }

    private void stopLocationUpdate() {
        if (this.locationManager != null)
            this.locationManager.removeUpdates(locationListener);
        this.finish();
    }

    private final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) return;
            Geolocation geolocation = new Geolocation(location);
            CurrentLocationProvider.this.output(geolocation);
            CurrentLocationProvider.this.stopLocationUpdate();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            CurrentLocationProvider.this.stopLocationUpdate();
        }

    };
}
