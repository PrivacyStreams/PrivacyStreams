package io.github.privacystreams.app.db

import android.content.ContentValues
import android.os.Build
import io.github.privacystreams.app.R
import io.github.privacystreams.app.db.PStreamContract.NotificationEntry
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.notification.Notification


class PSNotificationTable(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {
    override val iconResId: Int = R.drawable.notification

    override val sqlCreateEntry: String
        get() = "CREATE TABLE " + tableName + " (" +
                NotificationEntry.TIME_CREATED + " INTEGER PRIMARY KEY," +
                NotificationEntry.POST_TIME + " INTEGER," +
                NotificationEntry.ACTION + " TEXT," +
                NotificationEntry.CATEGORY + " TEXT, " +
                NotificationEntry.PACKAGE_NAME + " TEXT," +
                NotificationEntry.TITLE + " TEXT," +
                NotificationEntry.TEXT + " TEXT)"

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.uqi.getData(Notification.asUpdates(), this.purpose)
                    .logAs(this.tableName)
                    .forEach(object : Callback<Item>() {
                        override fun onInput(input: Item) {
                            val values = ContentValues()
                            values.put(NotificationEntry.TIME_CREATED, input.getAsLong(Notification.TIME_CREATED))
                            values.put(NotificationEntry.POST_TIME, input.getAsLong(Notification.POST_TIME))
                            values.put(NotificationEntry.ACTION, input.getAsString(Notification.ACTION))
                            values.put(NotificationEntry.CATEGORY, input.getAsString(Notification.CATEGORY))
                            values.put(NotificationEntry.PACKAGE_NAME, input.getAsString(Notification.PACKAGE_NAME))
                            values.put(NotificationEntry.TITLE, input.getAsString(Notification.TITLE))
                            values.put(NotificationEntry.TEXT, input.getAsString(Notification.TEXT))
                            db.insert(tableName, null, values)
                            tableStatus.increaseNumItems()
                        }
                    })
        }
    }

    override fun provide() {
        val db = dbHelper.readableDatabase
        val cur = db.query(this.tableName, null, null, null, null, null, null)
        while (cur.moveToNext()) {
            val item : Item = Item()
            item.setFieldValue(Notification.POST_TIME, cur.getLong(cur.getColumnIndex(NotificationEntry.POST_TIME)))
            item.setFieldValue(Notification.ACTION, cur.getString(cur.getColumnIndex(NotificationEntry.ACTION)))
            item.setFieldValue(Notification.CATEGORY, cur.getString(cur.getColumnIndex(NotificationEntry.CATEGORY)))
            item.setFieldValue(Notification.PACKAGE_NAME, cur.getString(cur.getColumnIndex(NotificationEntry.PACKAGE_NAME)))
            item.setFieldValue(Notification.TITLE, cur.getString(cur.getColumnIndex(NotificationEntry.TITLE)))
            item.setFieldValue(Notification.TEXT, cur.getString(cur.getColumnIndex(NotificationEntry.TEXT)))
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
