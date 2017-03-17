package com.github.privacystreams.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by fanglinchen on 3/15/17.
 */

public class LocationStayUpdatesProvider extends MultiItemStreamProvider {
    private String provider;
    private float minDistance;
    private String developerKey;

    private long startTimestamp;
    private long endTimestamp;

    private int minutesElapsed=0;

    // In meters
    private static final int VICINITY_THRESHOLD = 10;

    // A list of geopoints that are in a moving window of 9 minutes
    private LinkedList<GeoPoint> geoPoints = new LinkedList<>();

    // The trio of three averaged geopoints.
    // a is the averaged geopoint of geopoints collected in
    // the first three minutes,
    // b is the averaged geopoint of geopoints collected in
    // the second three minutes,
    // and c is the averaged geopoint of geopoints collected in
    // the last three minutes.
    private LinkedList<GeoPoint> abc = new LinkedList<>();


    private ArrayList<Double> coordinateArrayList = new ArrayList<>();
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();


    private final static int MOBILITY_STATUS_STAY = 0;
    private final static int MOBILITY_STATUS_MOVING = 1;

    private transient LocationManager locationManager;
    private transient LocationListener locationListener;

    private boolean isAlarmed_left = true;
    private int mobility_status = MOBILITY_STATUS_STAY;

    private static final int NEARBY_RADIUS = 80;
    private static final int ONE_MINUTE = 60*1000;

    private GeoPoint average(List<GeoPoint> geoPoints){
        double x = 0;
        double y = 0;
        for(GeoPoint geoPoint:geoPoints){
            x += geoPoint.getX();
            y += geoPoint.getY();
        }
        x = x/geoPoints.size();
        y = y/geoPoints.size();
        return new GeoPoint(x,y);
    }

    private void read(GeoPoint geoPoint){

        if(geoPoints.size() == 9) {
            geoPoints.remove(0);
        }

        geoPoints.add(geoPoint);

        // end of every three consecutive GeoPoints

        if( (minutesElapsed+1) % 3 == 0){
            if(abc.size() == 3){
                abc.remove(0);
                abc.add(average(geoPoints.subList(geoPoints.size()-3,
                        geoPoints.size()-1)));
            }
            else{
                abc.add(average(geoPoints.subList(geoPoints.size()-3,
                        geoPoints.size()-1)));
            }
        }
    }
    public  void process(GeoPoint geoPoint){

        read(geoPoint);
        minutesElapsed += 1;
        if(minutesElapsed>9){
            if( (minutesElapsed+1) % 3 == 0){
                statusJudgement();
            }
        }
    }

    private  void checkIfArriving(){
        //Not Alarmed
        if(isClose(abc.get(2),abc.get(1))){
            mobility_status = MOBILITY_STATUS_STAY;
            coordinateArrayList = abc.get(2).toList();
            generateAddressTypeList(abc.get(2));
            startTimestamp = System.currentTimeMillis() - ONE_MINUTE * 4;
        }
    }

    private void checkIfLeaving(){
        if(isAlarmed_left){

            boolean bcClose = isClose(abc.get(1),abc.get(2));
            boolean acClose = isClose(abc.get(0),abc.get(2));

            if(!acClose){
                if(bcClose){
                    // User arrives at a new location and stays
                    // there for the last 5-6 minutes.

                    endTimestamp = System.currentTimeMillis()-9 * ONE_MINUTE;
                    coordinateArrayList = abc.get(1).toList();
                    generateAddressTypeList(abc.get(1));

                    output(new LocationStay(coordinateArrayList,addressList,typeList,startTimestamp,endTimestamp));
                    startTimestamp = System.currentTimeMillis()- 4 * ONE_MINUTE;
                }
                else{
                    // User is moving continuously the whole nine minutes.

                    mobility_status = MOBILITY_STATUS_MOVING;
                    endTimestamp=System.currentTimeMillis()-9 * ONE_MINUTE;
                }
            }
            isAlarmed_left=false;

        }
        else{
            isAlarmed_left= !isClose(abc.get(2),abc.get(1));
        }
    }

    private void statusJudgement() {
        if(mobility_status == MOBILITY_STATUS_STAY){
            checkIfLeaving();
        }
        else{
            checkIfArriving();
        }
    }

    LocationStayUpdatesProvider(String provider,
                                float minDistance,
                                String developerKey){
        this.provider = provider;
        this.minDistance = minDistance;
        this.developerKey = developerKey;
        this.addParameters(provider,  minDistance, developerKey);

        if (StringUtils.equals(this.provider, LocationManager.GPS_PROVIDER)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        this.addRequiredPermissions(Manifest.permission.INTERNET);
    }

    private void getLocationUpdates() {
        locationManager.requestLocationUpdates(this.provider, ONE_MINUTE, this.minDistance, locationListener);
    }

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        this.locationManager.removeUpdates(locationListener);
    }

    private boolean isClose(GeoPoint a, GeoPoint b){
        return (distanceInMeters(a, b) < VICINITY_THRESHOLD);
    }

    private float distanceInMeters(GeoPoint a, GeoPoint b){
        float[] results= {};
        Location.distanceBetween(
                a.getX(),
                a.getY(),
                b.getX(),
                b.getY(),
                results);
        return results[0];
    }

    private String addressFetchingUrl(GeoPoint geoPoint){
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + geoPoint.getX() + ","
                + geoPoint.getY() +
                "&radius=" + NEARBY_RADIUS
                +"&key="+ developerKey;
    }

    private void generateAddressTypeList(GeoPoint geoPoint){
        addressList.clear();
        typeList.clear();
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(addressFetchingUrl(geoPoint));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            String JsonString = json.toString();
            JSONObject obj = new JSONObject(JsonString);
            JSONArray arr = obj.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getString("name");
                String types = arr.getJSONObject(i).getString("types");

                if (!types.contains("locality")
                        && !types.contains("sublocality")
                        && !types.contains("neighborhood")) {
                    addressList.add(name);
                    typeList.add(types);
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) return;

            process(new GeoPoint(location));
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
    @Override
    protected void provide() {
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationStayUpdatesProvider.MyLocationListener();
        Looper.prepare();
        this.getLocationUpdates();
    }
}
