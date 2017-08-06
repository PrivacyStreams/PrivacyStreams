package io.github.privacystreams.app.db

import android.content.ContentValues
import android.content.Context
import android.os.Build
import io.github.privacystreams.app.R
import io.github.privacystreams.app.db.PStreamContract.NotificationEntry
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.notification.Notification


class PSNotificationDBHelper(context: Context) : PStreamDBHelper(context, Notification::class.java) {
    override val iconResId: Int = R.drawable.notification

    override val sqlCreateEntries: String
        get() = "CREATE TABLE " + tableName + " (" +
                NotificationEntry.TIME_CREATED + " INTEGER PRIMARY KEY," +
                NotificationEntry.POST_TIME + " INTEGER," +
                NotificationEntry.ACTION + " TEXT," +
                NotificationEntry.CATEGORY + " TEXT, " +
                NotificationEntry.PACKAGE_NAME + " TEXT," +
                NotificationEntry.TITLE + " TEXT," +
                NotificationEntry.TEXT + " TEXT)"

    public override fun redirectPrivacyStreamsToDB() {
        val db = this.writableDatabase
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
                            dbStatus.increaseNumItems()
                        }
                    })
        }
    }

    override val dbStatus: PStreamDBStatus = staticStatus
    companion object {
        val staticStatus = PStreamDBStatus()
    }
}
