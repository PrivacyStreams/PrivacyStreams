package io.github.privacystreams.app.db

import android.content.ContentValues
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.device.DeviceEvent


class TableDeviceEvent(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "DeviceEvent"
        val ICON_RES_ID = R.drawable.smartphone

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = DeviceEvent.TIME_CREATED     // Long
        val TYPE = DeviceEvent.TYPE                     // String
        val EVENT = DeviceEvent.EVENT                   // String
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$TYPE TEXT," +
                "$EVENT TEXT)",
            "CREATE INDEX ${TABLE_NAME}_${TIME_CREATED}_index on $TABLE_NAME ($TIME_CREATED)",
            "CREATE INDEX ${TABLE_NAME}_${TYPE}_index on $TABLE_NAME ($TYPE)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(DeviceEvent.asUpdates(), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        values.put(TIME_CREATED, input.getAsLong(DeviceEvent.TIME_CREATED))
                        values.put(EVENT, input.getAsString(DeviceEvent.EVENT))
                        values.put(TYPE, input.getAsString(DeviceEvent.TYPE))
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
                item.setFieldValue(TYPE, cur.getString(cur.getColumnIndex(TYPE)))
                item.setFieldValue(EVENT, cur.getString(cur.getColumnIndex(EVENT)))
                output(item)
            }
            cur.close()
        }
    }
}
