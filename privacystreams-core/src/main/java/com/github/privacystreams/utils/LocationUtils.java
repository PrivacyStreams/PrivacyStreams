package com.github.privacystreams.utils;

import android.location.Location;

import com.github.privacystreams.location.LatLng;

import java.util.Random;

/**
 * A set of location-related utility functions.
 */

public class LocationUtils {
    public static LatLng distortLatLng(LatLng latLng, double radius) {
        double latitude = latLng.getLatitude();
        double longitude = latLng.getLongitude();
        Random random = new Random();
        double r = random.nextDouble() * radius;
        double theta = random.nextDouble() * Math.PI * 2;
        double distorted_latitude = latitude + r * Math.sin(theta) / getMetersPerLat(latitude);
        double distorted_longitude = longitude + r * Math.cos(theta) / getMetersPerLng(latitude);
        return new LatLng(distorted_latitude, distorted_longitude);
    }

    public static double getMetersPerLat(double latitude) {
//        return 111132.954 - 559.822 * Math.cos(2 * latitude * Math.PI / 180) + 1.175 * Math.cos(4 * latitude * Math.PI / 180);
        return 111111; // simplify
    }

    public static double getMetersPerLng(double latitude) {
        return 111132.954 * Math.cos(latitude * Math.PI / 180);
    }

    public static Double getDistanceBetween(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == null || latLng2 == null)
            return null;
        float[] result = new float[1];
        Location.distanceBetween(latLng1.getLatitude(), latLng1.getLongitude(),
                latLng2.getLatitude(), latLng2.getLongitude(), result);
        return (double) result[0];
    }
}
