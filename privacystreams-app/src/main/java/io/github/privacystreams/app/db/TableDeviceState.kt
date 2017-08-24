package io.github.privacystreams.app.db

import android.content.ContentValues
import io.github.privacystreams.app.Config
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.device.DeviceState


class TableDeviceState(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "DeviceState"
        val ICON_RES_ID = R.drawable.battery

        /* Fields */
        val _ID = "_id"                                     // Long
        val TIME_CREATED = DeviceState.TIME_CREATED         // Long
        val BATTERY_LEVEL = DeviceState.BATTERY_LEVEL       // Float
        val IS_CONNECTED = DeviceState.IS_CONNECTED         // Boolean
        val WIFI_BSSID = DeviceState.WIFI_BSSID             // String
        val IS_SCREEN_ON = DeviceState.IS_SCREEN_ON         // Boolean
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$BATTERY_LEVEL REAL," +
                "$IS_CONNECTED INTEGER," +
                "$WIFI_BSSID TEXT," +
                "$IS_SCREEN_ON INTEGER)",
            "CREATE INDEX ${TABLE_NAME}_${TIME_CREATED}_index on $TABLE_NAME ($TIME_CREATED)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(DeviceState.asUpdates(Config.COLLECT_DEVICE_STATE_INTERVAL, Config.COLLECT_DEVICE_STATE_MASK), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        values.put(TIME_CREATED, input.getAsLong(DeviceState.TIME_CREATED))
                        values.put(BATTERY_LEVEL, input.getAsFloat(DeviceState.BATTERY_LEVEL))
                        values.put(IS_CONNECTED, if (input.getAsBoolean(DeviceState.IS_CONNECTED)) 1 else 0)
                        values.put(WIFI_BSSID, input.getAsString(DeviceState.WIFI_BSSID))
                        values.put(IS_SCREEN_ON, if (input.getAsBoolean(DeviceState.IS_SCREEN_ON)) 1 else 0)
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
                item.setFieldValue(BATTERY_LEVEL, cur.getFloat(cur.getColumnIndex(BATTERY_LEVEL)))
                item.setFieldValue(IS_CONNECTED, cur.getInt(cur.getColumnIndex(IS_CONNECTED)) > 0)
                item.setFieldValue(WIFI_BSSID, cur.getString(cur.getColumnIndex(WIFI_BSSID)))
                item.setFieldValue(IS_SCREEN_ON, cur.getInt(cur.getColumnIndex(IS_SCREEN_ON)) > 0)
                output(item)
            }
            cur.close()
        }
    }
}
