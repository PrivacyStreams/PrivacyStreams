package io.github.privacystreams.app.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import io.github.privacystreams.app.Config
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.location.LatLon


class PSLocationDBHelper(context: Context) : PStreamDBHelper(context, Geolocation::class.java) {

    override protected val sqlCreateEntries: String
        get() = "CREATE TABLE " + tableName + " (" +
                GeolocationEntry.TIME_CREATED + " INTEGER PRIMARY KEY," +
                GeolocationEntry.LATITUDE + " REAL," +
                GeolocationEntry.LONGITUDE + " REAL, " +
                GeolocationEntry.PROVIDER + " TEXT," +
                GeolocationEntry.ACCURACY + " REAL," +
                GeolocationEntry.BEARING + " REAL," +
                GeolocationEntry.SPEED + " REAL)"

    public override fun redirectPrivacyStreamsToDB() {
        val db = this.writableDatabase
        this.uqi.getData(Geolocation.asUpdates(Config.COLLECT_LOCATION_INTERVAL, Config.COLLECT_LOCATION_LEVEL), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        val latLon = input.getValueByField<LatLon>(Geolocation.LAT_LON) ?: return
                        values.put(GeolocationEntry.TIME_CREATED, input.getAsLong(Geolocation.TIME_CREATED))
                        values.put(GeolocationEntry.LATITUDE, latLon.latitude)
                        values.put(GeolocationEntry.LONGITUDE, latLon.longitude)
                        values.put(GeolocationEntry.PROVIDER, input.getAsString(Geolocation.PROVIDER))
                        values.put(GeolocationEntry.ACCURACY, input.getAsFloat(Geolocation.ACCURACY))
                        values.put(GeolocationEntry.BEARING, input.getAsFloat(Geolocation.BEARING))
                        values.put(GeolocationEntry.SPEED, input.getAsFloat(Geolocation.SPEED))
                        db.insert(tableName, null, values)
                    }
                })
    }
}
