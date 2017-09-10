package io.github.privacystreams.location;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.utils.Globals;


public class LocationCluster {
    protected static List<GeoPoint> mGeoPoints = new ArrayList<>();
    private static GeoPoint mLastPoint;
    private static GeoPoint mThisPoint;
    private static LocationStay mLocationStay;
    private static int minutes = 0;
    private static long startTime = 0;
    private static long endTime = 0;
    private static GeoPoint averageGeoPoint = null;
    static boolean hasLeft = false;


    private static boolean isClose(GeoPoint a, GeoPoint b) {
        return distanceBetween(a, b) < Globals.LocationConfig.isCloseThreshold;
    }

    private static double distanceBetween(GeoPoint a, GeoPoint b) {
        return (a.getLat() - b.getLat()) * (a.getLat() - b.getLat())
                + (a.getLon() - b.getLon()) * (a.getLon() - b.getLon());
    }

    static void addNewGeoPoint(GeoPoint geoPoint) {
        mGeoPoints.add(geoPoint);
        if (mGeoPoints.size() == 1) {
            mThisPoint = mGeoPoints.get(0);
            averageGeoPoint = mThisPoint;
            startLookingForLocationStay();
        } else {
            mLastPoint = new GeoPoint(mThisPoint);
            mThisPoint = geoPoint;
            updateLocationStay();
        }
    }

    static void startLookingForLocationStay() {
        //?
        hasLeft = false;
        mLocationStay = new LocationStay();
        minutes = 1;
    }

    private static void updateLocationStay() {
        // The user stays at the location at least one minute ago.
        if (isClose(averageGeoPoint, mLastPoint)) {
            averageGeoPoint.setLat((averageGeoPoint.getLat() * minutes + mLastPoint.getLat()) / (minutes + 1));
            averageGeoPoint.setLon((averageGeoPoint.getLon() * minutes + mLastPoint.getLon()) / (minutes + 1));
            minutes++;
        }
        // The user starts leaving from the previous location one minute ago.
        else {
            // But the user is back now at this minute,
            // we then consider the user did not leave the previous stay at all
            // because there was probably an error fetching the last location.

            if (isClose(averageGeoPoint, mThisPoint)) {
                averageGeoPoint.setTimestamp(mLastPoint.getTimestamp());
                mLastPoint = new GeoPoint(averageGeoPoint);
                minutes++;
            } else {
                // The user has left the previous location more than two minutes
                if (minutes >= 5) {
                    hasLeft = true;
                    //?????
                    endTime = mGeoPoints.size() - 2;
                    mLocationStay.setFieldValue(LocationStay.LATITUDE_AVERAGE, averageGeoPoint.getLat());
                    mLocationStay.setFieldValue(LocationStay.LONGITUDE_AVERAGE, averageGeoPoint.getLon());
                    mLocationStay.setFieldValue(LocationStay.START_TIMESTAMP, startTime);
                    mLocationStay.setFieldValue(LocationStay.END_TIMESTAMP, endTime);
                }

                averageGeoPoint = new GeoPoint(mThisPoint);
                //?????
                startTime = mGeoPoints.size() - 2;
                startLookingForLocationStay();
            }
        }
    }

    static LocationStay getLocationStay() {
        return mLocationStay;
    }
}
