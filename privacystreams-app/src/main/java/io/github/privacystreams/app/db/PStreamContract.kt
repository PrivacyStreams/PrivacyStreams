package io.github.privacystreams.app.db

import android.provider.BaseColumns
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.notification.Notification

object PStreamContract {
    val DB_VERSION = 1
    val DB_NAME = "privacystreams.db"

    class GeolocationEntry : BaseColumns {
        companion object {
            val TIME_CREATED = Geolocation.TIME_CREATED     // Long
            val TIMESTAMP = Geolocation.TIMESTAMP           // Long
            val BEARING = Geolocation.BEARING               // Float
            val ACCURACY = Geolocation.ACCURACY             // Float
            val SPEED = Geolocation.SPEED                   // Float
            val PROVIDER = Geolocation.PROVIDER             // String
            val LATITUDE = "latitude"                       // Double
            val LONGITUDE = "longitude"                     // Double
        }
    }

    class NotificationEntry : BaseColumns {
        companion object {
            val TIME_CREATED = Notification.TIME_CREATED    // Long
            val POST_TIME = Notification.POST_TIME          // Long
            val ACTION = Notification.ACTION                // String
            val CATEGORY = Notification.CATEGORY            // String
            val PACKAGE_NAME = Notification.PACKAGE_NAME    // String
            val TITLE = Notification.TITLE                  // String
            val TEXT = Notification.TEXT                    // String
        }
    }
}
