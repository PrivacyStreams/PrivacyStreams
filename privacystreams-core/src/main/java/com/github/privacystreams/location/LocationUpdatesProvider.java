package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

import org.apache.commons.lang3.StringUtils;

/**
 * Provide location updates with Android standard APIs.
 */

final class LocationUpdatesProvider extends MStreamProvider {

    private String provider;
    private long minTime;
    private float minDistance;
    private transient LocationManager locationManager;
    private transient LocationListener locationListener;

    LocationUpdatesProvider(String provider, long minTime, float minDistance) {
        this.provider = provider;
        this.minTime = minTime;
        this.minDistance = minDistance;
        this.addParameters(provider, minTime, minDistance);

        if (StringUtils.equals(this.provider, LocationManager.GPS_PROVIDER)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    protected void provide() {
        Log.e("prepare","provid");
        Looper.prepare();
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        this.getLocationUpdates();
        Looper.loop();
    }

    private void getLocationUpdates() {
        locationManager.requestLocationUpdates(this.provider, this.minTime, this.minDistance, locationListener);
    }

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        this.locationManager.removeUpdates(locationListener);
    }

    private final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) return;
            GeoLocation geoLocation = new GeoLocation(location);
            LocationUpdatesProvider.this.output(geoLocation);
            Log.e("add","location");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };
}
