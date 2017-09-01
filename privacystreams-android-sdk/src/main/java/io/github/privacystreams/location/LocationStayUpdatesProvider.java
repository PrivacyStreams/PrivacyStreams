package io.github.privacystreams.location;

import android.location.Location;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.github.privacystreams.location.GoogleLocationUpdatesProvider;
import io.github.privacystreams.utils.Logging;

/**
 * Created by xiaobing1117 on 2017/9/1.
 */

public class LocationStayUpdatesProvider extends GoogleLocationUpdatesProvider {
    private LocationStay mLocationStay = null;
    private String mApiKey;
    public LocationStayUpdatesProvider(int interval, String level, String apiKey){
        super(interval,level,apiKey);
        mApiKey = apiKey;
    }

    @Override
    public void provide(){
        super.provide();
    }

    @Override
    public void addGeoPoint(Location location){
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

    protected class MyTask extends AsyncTask<String,Void,String> {

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
