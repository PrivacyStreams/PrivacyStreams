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
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * location asUpdates
 */

final class LocationUpdatesProvider extends MultiItemStreamProvider {

    private String provider;
    private long minTime;
    private float minDistance;

    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;

    LocationUpdatesProvider(String provider, long minTime, float minDistance) {
        this.provider = provider;
        this.minTime = minTime;
        this.minDistance = minDistance;
        this.addParameters(provider, minTime, minDistance);
        this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    protected void provide(MultiItemStream output) {
        context = this.getContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(output);

        this.getLocationUpdates(output);
    }

    private void getLocationUpdates(MultiItemStream stream) {
        Context context = this.getContext();
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
            Logging.warn("Need location permissions.");
            stream.write(null);
            return;
        }
        locationManager.requestLocationUpdates(this.provider, this.minTime, this.minDistance, locationListener);
    }

    @Override
    protected void onStop(Void input, MultiItemStream output) {
        super.onStop(input, output);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Logging.warn("Need location permissions.");
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private final class MyLocationListener implements LocationListener {
        private MultiItemStream stream;

        MyLocationListener(MultiItemStream stream) {
            this.stream = stream;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) return;
            GeoLocation geoLocation = new GeoLocation(location);
            this.stream.write(geoLocation);
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
