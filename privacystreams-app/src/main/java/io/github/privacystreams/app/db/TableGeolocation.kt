package io.github.privacystreams.app.db

import android.content.ContentValues
import io.github.privacystreams.app.Config
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.location.LatLon


class TableGeolocation(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "Geolocation"
        val ICON_RES_ID = R.drawable.location

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = Geolocation.TIME_CREATED     // Long
        val TIMESTAMP = Geolocation.TIMESTAMP           // Long
        val BEARING = Geolocation.BEARING               // Float
        val ACCURACY = Geolocation.ACCURACY             // Float
        val SPEED = Geolocation.SPEED                   // Float
        val PROVIDER = Geolocation.PROVIDER             // String
        val LATITUDE = "latitude"                       // Double
        val LONGITUDE = "longitude"                     // Double
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY, " +
                "$TIME_CREATED INTEGER," +
                "$TIMESTAMP INTEGER," +
                "$LATITUDE REAL," +
                "$LONGITUDE REAL, " +
                "$PROVIDER TEXT," +
                "$ACCURACY REAL," +
                "$BEARING REAL," +
                "$SPEED REAL)",
            "CREATE INDEX ${TABLE_NAME}_${TIME_CREATED}_index on $TABLE_NAME ($TIME_CREATED)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(Geolocation.asUpdates(Config.COLLECT_LOCATION_INTERVAL, Config.COLLECT_LOCATION_LEVEL), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        val latLon = input.getValueByField<LatLon>(Geolocation.LAT_LON) ?: return
                        values.put(TIME_CREATED, input.getAsLong(Geolocation.TIME_CREATED))
                        values.put(TIMESTAMP, input.getAsLong(Geolocation.TIMESTAMP))
                        values.put(LATITUDE, latLon.latitude)
                        values.put(LONGITUDE, latLon.longitude)
                        values.put(PROVIDER, input.getAsString(Geolocation.PROVIDER))
                        values.put(ACCURACY, input.getAsFloat(Geolocation.ACCURACY))
                        values.put(BEARING, input.getAsFloat(Geolocation.BEARING))
                        values.put(SPEED, input.getAsFloat(Geolocation.SPEED))
                        db.insert(tableName, null, values)
                        increaseNumItems()
                    }

                    override fun onFail(exception: PSException) {
                        stopCollectService()
                        if (exception.isPermissionDenied) {
                            message.set("Denied")
                        }
                    }
                })
    }

    class PROVIDER(): PStreamTableProvider() {
        override fun provide() {
            val dbHelper = PStreamDBHelper.getInstance(context)
            val db = dbHelper.readableDatabase
            val cur = db.query(TABLE_NAME, null, null, null, null, null, null)
            while (cur.moveToNext()) {
                val item : Item = Item()
                item.setFieldValue(TIME_CREATED, cur.getLong(cur.getColumnIndex(TIME_CREATED)))
                item.setFieldValue(TIMESTAMP, cur.getLong(cur.getColumnIndex(TIMESTAMP)))
                item.setFieldValue(PROVIDER, cur.getString(cur.getColumnIndex(PROVIDER)))
                item.setFieldValue(SPEED, cur.getFloat(cur.getColumnIndex(SPEED)))
                item.setFieldValue(ACCURACY, cur.getFloat(cur.getColumnIndex(ACCURACY)))
                item.setFieldValue(BEARING, cur.getFloat(cur.getColumnIndex(BEARING)))
                item.setFieldValue(Geolocation.LAT_LON, LatLon(
                        cur.getDouble(cur.getColumnIndex(LATITUDE)),
                        cur.getDouble(cur.getColumnIndex(LONGITUDE))
                ))
                output(item)
            }
            cur.close()
        }
    }
}
