package io.github.privacystreams.location;

import android.Manifest;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.privacystreams.core.R;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Assertions;
import io.github.privacystreams.utils.Logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * Provide location updates with Google API.
 */
class GoogleLocationUpdatesProvider extends PStreamProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String LOG_TAG = "[GoogleLocationUpdatesProvider]";

    private final long interval;
    private final String level;
    private boolean isCluster = false;
    private LocationStay mLocationStay;
    private String mApiKey;

    protected GoogleLocationUpdatesProvider(long interval, String level) {
        this.interval = interval;
        this.level = Assertions.notNull("level", level);
        this.addParameters(interval, level);
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    protected GoogleLocationUpdatesProvider(long interval, String level, String api_key) {
        this.interval = interval;
        this.isCluster = true;
        mApiKey = api_key;
        Logging.error("google location updates");
        this.level = Assertions.notNull("level", level);
        this.addParameters(interval, level);
        if (Geolocation.LEVEL_EXACT.equals(level)) {
            this.addRequiredPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
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

    public void addGeoPoint(Location location) {
        double x = location.getLatitude();
        double y = location.getLongitude();
        long timestamp = location.getTime();
        GeoPoint point = new GeoPoint(x,y,timestamp);
        LocationCluster.addLocation(point);
        if(LocationCluster.isLocationStayOver){
            mLocationStay = LocationCluster.getLocationStay();
            LocationCluster.initLocationStay();
            double stayX = mLocationStay.getValueByField(LocationStay.X);
            double stayY = mLocationStay.getValueByField(LocationStay.Y);
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + stayX + "," + stayY + "&radius=20&key="+mApiKey;
            new MyTask().execute(url);
        }
    }



    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        if(isCluster) {
            if(location == null)
                return;
            addGeoPoint(location);
        }
        else {
            if (location==null)
                return;
            this.output(new Geolocation(location));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        stopLocationUpdate();
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


    private void startLocationUpdate() {
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

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        this.finish();
    }

    protected class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String...params){
            String url = params[0];
            String json = "";
            try {
                URL mUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection)mUrl.openConnection();
                urlConnection.setDoInput(true);
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = br.readLine()) != null) {
                    json+=line+"\n";
                }
                Logging.error("json is:"+json);
            }catch(Exception e){
                Logging.error("request error!:"+e.getMessage());
            }

            return json;
        }

        @Override
        protected void onPostExecute(String address){
            mLocationStay.setFieldValue(LocationStay.ADDRESS,address);
            output(mLocationStay);
        }
    }
}
