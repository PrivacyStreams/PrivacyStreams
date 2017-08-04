package io.github.privacystreams.app.providers

import android.content.ContentValues
import io.github.privacystreams.app.db.PSLocationDBHelper
import io.github.privacystreams.app.db.PStreamContract
import io.github.privacystreams.app.db.PStreamDBHelper
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.PStreamProvider
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.location.LatLon
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry

/**
 * Provide historic location data from database.
 */
class HistoricLocationProvider() : PStreamDBProvider() {
    override val dbHelper: PStreamDBHelper
        get() = PSLocationDBHelper(this.uqi.context)

    override fun provide() {
        val db = dbHelper.readableDatabase
        val cur = db.query(dbHelper.tableName, null, null, null, null, null, null)
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

}
