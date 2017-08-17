package io.github.privacystreams.app.db

import android.content.ContentValues
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import io.github.privacystreams.accessibility.AccEvent
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.utils.IOUtils


class TableUIEvent(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "UIEvent"
        val ICON_RES_ID = R.drawable.interaction

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = AccEvent.TIME_CREATED        // Long
        val EVENT_TIME = AccEvent.EVENT_TIME            // Long
        val EVENT_TYPE = AccEvent.EVENT_TYPE            // Long
        val PACKAGE_NAME = AccEvent.PACKAGE_NAME        // String
        val ROOT_NODE_JSON = "root_node_json"           // String
        val EVENT_JSON = "event_json"                   // String
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$EVENT_TYPE INTEGER," +
                "$EVENT_TIME INTEGER," +
                "$PACKAGE_NAME TEXT," +
                "$ROOT_NODE_JSON TEXT," +
                "$EVENT_JSON TEXT)",
            "CREATE INDEX ${TABLE_NAME}_time_created_index on $TABLE_NAME ($TIME_CREATED)",
            "CREATE INDEX ${TABLE_NAME}_event_time_index on $TABLE_NAME ($EVENT_TIME)",
            "CREATE INDEX ${TABLE_NAME}_package_name_index on $TABLE_NAME ($PACKAGE_NAME)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(AccEvent.asUIActions(), this.purpose)
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val values = ContentValues()
                        val accEvent: AccessibilityEvent = input.getValueByField(AccEvent.EVENT)
                        val rootNode: AccessibilityNodeInfo? = input.getValueByField(AccEvent.ROOT_NODE)
                        val rootNodeStr = "" + IOUtils.serialize(rootNode)
                        val accEventStr = "" + IOUtils.serialize(accEvent)
                        values.put(TIME_CREATED, input.getAsLong(AccEvent.TIME_CREATED))
                        values.put(EVENT_TYPE, input.getAsInteger(AccEvent.EVENT_TYPE))
                        values.put(EVENT_TIME, input.getAsLong(AccEvent.EVENT_TIME))
                        values.put(PACKAGE_NAME, input.getAsString(AccEvent.PACKAGE_NAME))
                        values.put(ROOT_NODE_JSON, rootNodeStr)
                        values.put(EVENT_JSON, accEventStr)
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
                item.setFieldValue(EVENT_TIME, cur.getLong(cur.getColumnIndex(EVENT_TIME)))
                item.setFieldValue(EVENT_TYPE, cur.getInt(cur.getColumnIndex(EVENT_TYPE)))
                item.setFieldValue(PACKAGE_NAME, cur.getString(cur.getColumnIndex(PACKAGE_NAME)))
                item.setFieldValue(ROOT_NODE_JSON, cur.getString(cur.getColumnIndex(ROOT_NODE_JSON)))
                item.setFieldValue(EVENT_JSON, cur.getString(cur.getColumnIndex(EVENT_JSON)))
                output(item)
            }
            cur.close()
        }
    }
}
