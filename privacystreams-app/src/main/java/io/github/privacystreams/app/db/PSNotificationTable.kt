package io.github.privacystreams.app.db

import android.content.ContentValues
import android.os.Build
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.notification.Notification


class PSNotificationTable(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = PSNotificationTable::class.java.simpleName
        val ICON_RES_ID = R.drawable.notification
        val TABLE_STATUS = PStreamTableStatus()

        /* Fields */
        val TIME_CREATED = Notification.TIME_CREATED    // Long
        val POST_TIME = Notification.POST_TIME          // Long
        val ACTION = Notification.ACTION                // String
        val CATEGORY = Notification.CATEGORY            // String
        val PACKAGE_NAME = Notification.PACKAGE_NAME    // String
        val TITLE = Notification.TITLE                  // String
        val TEXT = Notification.TEXT                    // String
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID
    override val tableStatus: PStreamTableStatus = TABLE_STATUS

    override val sqlCreateEntry: String
        get() = "CREATE TABLE " + tableName + " (" +
                TIME_CREATED + " INTEGER PRIMARY KEY," +
                POST_TIME + " INTEGER," +
                ACTION + " TEXT," +
                CATEGORY + " TEXT, " +
                PACKAGE_NAME + " TEXT," +
                TITLE + " TEXT," +
                TEXT + " TEXT)"

    override val sqlDeleteEntry: String
        get() = "DROP TABLE IF EXISTS " + tableName

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
                            db.insert(tableName, null, values)
                            tableStatus.increaseNumItems()
                        }
                    })
        }
    }

    class PROVIDER(): PStreamTableProvider() {
        override fun provide() {
            val dbHelper = PStreamDBHelper(context)
            val db = dbHelper.readableDatabase
            val cur = db.query(TABLE_NAME, null, null, null, null, null, null)
            while (cur.moveToNext()) {
                val item : Item = Item()
                item.setFieldValue(Notification.POST_TIME, cur.getLong(cur.getColumnIndex(POST_TIME)))
                item.setFieldValue(Notification.ACTION, cur.getString(cur.getColumnIndex(ACTION)))
                item.setFieldValue(Notification.CATEGORY, cur.getString(cur.getColumnIndex(CATEGORY)))
                item.setFieldValue(Notification.PACKAGE_NAME, cur.getString(cur.getColumnIndex(PACKAGE_NAME)))
                item.setFieldValue(Notification.TITLE, cur.getString(cur.getColumnIndex(TITLE)))
                item.setFieldValue(Notification.TEXT, cur.getString(cur.getColumnIndex(TEXT)))
                output(item)
            }
            cur.close()
            db.close()
            dbHelper.close()
        }
    }
}
