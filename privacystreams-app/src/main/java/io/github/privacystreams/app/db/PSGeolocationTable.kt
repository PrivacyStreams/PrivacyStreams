package io.github.privacystreams.app.db

import android.content.ContentValues
import io.github.privacystreams.app.Config
import io.github.privacystreams.app.R
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.location.LatLon


class PSGeolocationTable(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {
    override val iconResId: Int = R.drawable.location

    override val sqlCreateEntry: String
        get() = "CREATE TABLE " + tableName + " (" +
                GeolocationEntry.TIME_CREATED + " INTEGER PRIMARY KEY," +
                GeolocationEntry.TIMESTAMP + " INTEGER," +
                GeolocationEntry.LATITUDE + " REAL," +
                GeolocationEntry.LONGITUDE + " REAL, " +
                GeolocationEntry.PROVIDER + " TEXT," +
                GeolocationEntry.ACCURACY + " REAL," +
                GeolocationEntry.BEARING + " REAL," +
                GeolocationEntry.SPEED + " REAL)"

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(Geolocation.asUpdates(Config.COLLECT_LOCATION_INTERVAL, Config.COLLECT_LOCATION_LEVEL), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        val latLon = input.getValueByField<LatLon>(Geolocation.LAT_LON) ?: return
                        values.put(GeolocationEntry.TIME_CREATED, input.getAsLong(Geolocation.TIME_CREATED))
                        values.put(GeolocationEntry.TIMESTAMP, input.getAsLong(Geolocation.TIMESTAMP))
                        values.put(GeolocationEntry.LATITUDE, latLon.latitude)
                        values.put(GeolocationEntry.LONGITUDE, latLon.longitude)
                        values.put(GeolocationEntry.PROVIDER, input.getAsString(Geolocation.PROVIDER))
                        values.put(GeolocationEntry.ACCURACY, input.getAsFloat(Geolocation.ACCURACY))
                        values.put(GeolocationEntry.BEARING, input.getAsFloat(Geolocation.BEARING))
                        values.put(GeolocationEntry.SPEED, input.getAsFloat(Geolocation.SPEED))
                        db.insert(tableName, null, values)
                        tableStatus.increaseNumItems()
                    }
                })
    }

    override fun provide() {
        val db = dbHelper.readableDatabase
        val cur = db.query(this.tableName, null, null, null, null, null, null)
        while (cur.moveToNext()) {
            val item : Item = Item()
            item.setFieldValue(Geolocation.TIMESTAMP, cur.getLong(cur.getColumnIndex(GeolocationEntry.TIMESTAMP)))
            item.setFieldValue(Geolocation.PROVIDER, cur.getString(cur.getColumnIndex(GeolocationEntry.PROVIDER)))
            item.setFieldValue(Geolocation.SPEED, cur.getFloat(cur.getColumnIndex(GeolocationEntry.SPEED)))
            item.setFieldValue(Geolocation.ACCURACY, cur.getFloat(cur.getColumnIndex(GeolocationEntry.ACCURACY)))
            item.setFieldValue(Geolocation.BEARING, cur.getFloat(cur.getColumnIndex(GeolocationEntry.BEARING)))
            item.setFieldValue(Geolocation.LAT_LON, LatLon(
                    cur.getDouble(cur.getColumnIndex(GeolocationEntry.LATITUDE)),
                    cur.getDouble(cur.getColumnIndex(GeolocationEntry.LONGITUDE))
            ))
            output(item)
        }
        cur.close()
        db.close()
        dbHelper.close()
    }

    override val tableStatus: PStreamTableStatus = TABLE_STATUS
    companion object {
        val TABLE_STATUS = PStreamTableStatus()
    }
}
