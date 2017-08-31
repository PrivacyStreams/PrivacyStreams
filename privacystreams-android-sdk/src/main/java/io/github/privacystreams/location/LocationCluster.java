package io.github.privacystreams.location;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.utils.Logging;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.getExponent;
import static java.lang.Math.sin;


public class LocationCluster {
    protected static List<GeoPoint> mGeoPoints = new ArrayList<>();
    private static GeoPoint mLastPoint;
    private static GeoPoint mThisPoint;
    private static final double RADIUS = 0.0000005;//around 5 meters
    private static LocationStay mLocationStay;
    private static int minutes = 0;
    private static long startTime = 0;
    private static long endTime = 0;
    private static GeoPoint averageLocation = null;
    public static boolean isLocationStayOver = false;


    private static boolean isClose(GeoPoint a, GeoPoint b){
        return distanceBetween(a,b)<RADIUS;
    }

    private static double distanceBetween(GeoPoint a, GeoPoint b){
        return (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y);
    }

    protected static void addLocation(GeoPoint geoPoint){
        mGeoPoints.add(geoPoint);
        updateGeoPoints();
    }

    private static void updateGeoPoints(){

        int size = mGeoPoints.size();
        switch (size){
            case 1:
                mThisPoint = mGeoPoints.get(0);
                averageLocation  = mThisPoint;
                initLocationStay();
                break;
            default:
                mLastPoint = new GeoPoint(mThisPoint);
                mThisPoint = new GeoPoint(mGeoPoints.get(size-1));
                updateLocationStay();

        }
    }

    protected static void initLocationStay(){

        isLocationStayOver = false;
        mLocationStay = new LocationStay();
        minutes = 1;
    }

    private static void updateLocationStay(){
        int size = mGeoPoints.size();
        if(isClose(averageLocation,mLastPoint)){
            averageLocation.x = (averageLocation.x*minutes + mLastPoint.x)/(minutes+1);
            averageLocation.y = (averageLocation.y*minutes + mLastPoint.y)/(minutes+1);
            minutes++;
        }else{
            if(isClose(averageLocation,mThisPoint)){
                averageLocation.timestamp = mLastPoint.timestamp;
                mLastPoint = new GeoPoint(averageLocation);
                minutes++;
            }else{
                if(minutes >= 5){
                    isLocationStayOver = true;
                    endTime = mGeoPoints.size()-2;
                    mLocationStay.setFieldValue(LocationStay.X,averageLocation.x);
                    mLocationStay.setFieldValue(LocationStay.Y,averageLocation.x);
                    mLocationStay.setFieldValue(LocationStay.START_TIMESTAMP,startTime);
                    mLocationStay.setFieldValue(LocationStay.END_TIMESTAMP,endTime);
                    Logging.error("a new localstay: from"+startTime+"to"+endTime);
                }
                averageLocation = new GeoPoint(mThisPoint);
                startTime = mGeoPoints.size()-2;
                initLocationStay();
            }
        }
    }

    public static LocationStay getLocationStay(){
        return mLocationStay;
    }
}
