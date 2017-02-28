package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by yuanchun on 21/11/2016.
 * location asUpdates
 */

final class LocationUpdatesProvider extends MultiItemStreamProvider {

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
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        this.getLocationUpdates();
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
