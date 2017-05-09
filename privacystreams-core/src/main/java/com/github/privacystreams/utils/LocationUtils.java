package com.github.privacystreams.utils;

import android.location.Location;

import com.github.privacystreams.location.LatLon;

import java.util.Random;

/**
 * A set of location-related utility functions.
 */

public class LocationUtils {
    public static LatLon distortLatLon(LatLon latLon, double radius) {
        double latitude = latLon.getLatitude();
        double longitude = latLon.getLongitude();
        Random random = new Random();
        double r = random.nextDouble() * radius;
        double theta = random.nextDouble() * Math.PI * 2;
        double distorted_latitude = latitude + r * Math.sin(theta) / getMetersPerLat(latitude);
        double distorted_longitude = longitude + r * Math.cos(theta) / getMetersPerLng(latitude);
        return new LatLon(distorted_latitude, distorted_longitude);
    }

    public static double getMetersPerLat(double latitude) {
//        return 111132.954 - 559.822 * Math.cos(2 * latitude * Math.PI / 180) + 1.175 * Math.cos(4 * latitude * Math.PI / 180);
        return 111111; // simplify
    }

    public static double getMetersPerLng(double latitude) {
        return 111132.954 * Math.cos(latitude * Math.PI / 180);
    }

    public static Double getDistanceBetween(LatLon latLon1, LatLon latLon2) {
        if (latLon1 == null || latLon2 == null)
            return null;
        float[] result = new float[1];
        Location.distanceBetween(latLon1.getLatitude(), latLon1.getLongitude(),
                latLon2.getLatitude(), latLon2.getLongitude(), result);
        return (double) result[0];
    }
}
