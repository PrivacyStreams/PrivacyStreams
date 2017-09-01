package io.github.privacystreams.location;

import android.location.Location;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;


public class LocationStayUpdatesProvider extends GoogleLocationUpdatesProvider {
    private static final String GOOGLE_PLACE_NEARBY_API_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private LocationStay mLocationStay;

    // The api key is used to fetch nearby place addresses.
    private String mApiKey;

    public LocationStayUpdatesProvider(int interval, String level, String apiKey){
        super(interval,level);
        mApiKey = apiKey;
    }

    @Override
    public void provide(){
        super.provide();
    }

    @Override
    public void onLocationChanged(Location location){
        LocationCluster.addNewGeoPoint(new GeoPoint(location.getLatitude(),
                location.getLongitude(), location.getTime()));

        if(LocationCluster.hasLeft){
            mLocationStay = LocationCluster.getLocationStay();
            LocationCluster.startLookingForLocationStay();

            String url = GOOGLE_PLACE_NEARBY_API_BASE_URL
                    + mLocationStay.getValueByField(LocationStay.LATITUDE_AVERAGE) + "," +mLocationStay.getValueByField(LocationStay.LONGITUDE_AVERAGE)
                    + "&radius=" + Globals.LocationConfig.nearbyPoiRadius
                    + "&key="+ mApiKey;

            new FetchNearbyAddressTask().execute(url);
        }
    }

    private class FetchNearbyAddressTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String...params){
            String json = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    json += line + "\n";
                }

            }
            catch(Exception e){
                Logging.error("request error!:"+e.getMessage());
            }

            return json;
        }

        @Override
        protected void onPostExecute(String address){
            mLocationStay.setFieldValue(LocationStay.ADDRESS, address);
            output(mLocationStay);
        }
    }
}
