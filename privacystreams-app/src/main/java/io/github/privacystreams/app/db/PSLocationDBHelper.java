package io.github.privacystreams.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.github.privacystreams.app.Config;
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.location.Geolocation;
import io.github.privacystreams.location.LatLon;


public class PSLocationDBHelper extends PStreamDBHelper {

    public PSLocationDBHelper(Context context) {
        super(context, Geolocation.class);
    }

    @Override
    protected String getSqlCreateEntries() {
        return "CREATE TABLE " + tableName + " (" +
                GeolocationEntry.TIME_CREATED + " INTEGER PRIMARY KEY," +
                GeolocationEntry.LATITUDE + " REAL," +
                GeolocationEntry.LONGITUDE + " REAL, " +
                GeolocationEntry.PROVIDER + " TEXT," +
                GeolocationEntry.ACCURACY + " REAL," +
                GeolocationEntry.BEARING + " REAL," +
                GeolocationEntry.SPEED + " REAL)";
    }

    public void redirectPrivacyStreamsToDB() {
        final SQLiteDatabase db = this.getWritableDatabase();
        this.uqi.getData(Geolocation.asUpdates(Config.COLLECT_LOCATION_INTERVAL, Config.COLLECT_LOCATION_LEVEL), this.purpose)
                .logAs(this.tableName)
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        ContentValues values = new ContentValues();
                        LatLon latLon = input.getValueByField(Geolocation.LAT_LON);
                        if (latLon == null) return;
                        values.put(GeolocationEntry.TIME_CREATED, input.getAsLong(Geolocation.TIME_CREATED));
                        values.put(GeolocationEntry.LATITUDE, latLon.getLatitude());
                        values.put(GeolocationEntry.LONGITUDE, latLon.getLongitude());
                        values.put(GeolocationEntry.PROVIDER, input.getAsString(Geolocation.PROVIDER));
                        values.put(GeolocationEntry.ACCURACY, input.getAsFloat(Geolocation.ACCURACY));
                        values.put(GeolocationEntry.BEARING, input.getAsFloat(Geolocation.BEARING));
                        values.put(GeolocationEntry.SPEED, input.getAsFloat(Geolocation.SPEED));
                        db.insert(tableName, null, values);
                    }
                });
    }
}
