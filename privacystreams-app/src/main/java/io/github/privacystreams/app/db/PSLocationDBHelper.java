package io.github.privacystreams.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.location.Geolocation;


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
}
