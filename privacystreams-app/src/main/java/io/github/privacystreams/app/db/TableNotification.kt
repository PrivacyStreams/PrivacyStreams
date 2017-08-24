package io.github.privacystreams.app.db

import android.content.ContentValues
import android.os.Build
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.notification.Notification


class TableNotification(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "Notification"
        val ICON_RES_ID = R.drawable.notification

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = Notification.TIME_CREATED    // Long
        val POST_TIME = Notification.POST_TIME          // Long
        val ACTION = Notification.ACTION                // String
        val CATEGORY = Notification.CATEGORY            // String
        val PACKAGE_NAME = Notification.PACKAGE_NAME    // String
        val TITLE = Notification.TITLE                  // String
        val TEXT = Notification.TEXT                    // String
        val SUB_TEXT = Notification.SUB_TEXT            // String
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$POST_TIME INTEGER," +
                "$ACTION TEXT," +
                "$CATEGORY TEXT, " +
                "$PACKAGE_NAME TEXT," +
                "$TITLE TEXT," +
                "$TEXT TEXT," +
                "$SUB_TEXT TEXT)",
            "CREATE INDEX ${TABLE_NAME}_${TIME_CREATED}_index on $TABLE_NAME ($TIME_CREATED)",
            "CREATE INDEX ${TABLE_NAME}_${POST_TIME}_index on $TABLE_NAME ($POST_TIME)",
            "CREATE INDEX ${TABLE_NAME}_${PACKAGE_NAME}_index on $TABLE_NAME ($PACKAGE_NAME)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.uqi.getData(Notification.asUpdates(), this.purpose)
                    .logAs(this.tableName)
                    .forEach(object : Callback<Item>() {
                        override fun onInput(input: Item) {
                            val values = ContentValues()
                            values.put(TIME_CREATED, input.getAsLong(Notification.TIME_CREATED))
                            values.put(POST_TIME, input.getAsLong(Notification.POST_TIME))
                            values.put(ACTION, input.getAsString(Notification.ACTION))
                            values.put(CATEGORY, input.getAsString(Notification.CATEGORY))
                            values.put(PACKAGE_NAME, input.getAsString(Notification.PACKAGE_NAME))
                            values.put(TITLE, input.getAsString(Notification.TITLE))
                            values.put(TEXT, input.getAsString(Notification.TEXT))
                            values.put(SUB_TEXT, input.getAsString(Notification.SUB_TEXT))
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
    }

    class PROVIDER(): PStreamTableProvider() {
        override fun provide() {
            val dbHelper = PStreamDBHelper.getInstance(context)
            val db = dbHelper.readableDatabase
            val cur = db.query(TABLE_NAME, null, null, null, null, null, null)
            while (cur.moveToNext()) {
                val item : Item = Item()
                item.setFieldValue(TIME_CREATED, cur.getLong(cur.getColumnIndex(TIME_CREATED)))
                item.setFieldValue(POST_TIME, cur.getLong(cur.getColumnIndex(POST_TIME)))
                item.setFieldValue(ACTION, cur.getString(cur.getColumnIndex(ACTION)))
                item.setFieldValue(CATEGORY, cur.getString(cur.getColumnIndex(CATEGORY)))
                item.setFieldValue(PACKAGE_NAME, cur.getString(cur.getColumnIndex(PACKAGE_NAME)))
                item.setFieldValue(TITLE, cur.getString(cur.getColumnIndex(TITLE)))
                item.setFieldValue(TEXT, cur.getString(cur.getColumnIndex(TEXT)))
                item.setFieldValue(SUB_TEXT, cur.getString(cur.getColumnIndex(SUB_TEXT)))
                output(item)
            }
            cur.close()
        }
    }
}
