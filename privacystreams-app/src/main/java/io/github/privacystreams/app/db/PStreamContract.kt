package io.github.privacystreams.app.db

import android.provider.BaseColumns

import io.github.privacystreams.core.Item
import io.github.privacystreams.location.Geolocation

object PStreamContract {

    class GeolocationEntry : BaseColumns {
        companion object {
            val TIME_CREATED = Item.TIME_CREATED
            val BEARING = Geolocation.BEARING
            val ACCURACY = Geolocation.ACCURACY
            val SPEED = Geolocation.SPEED
            val PROVIDER = Geolocation.PROVIDER
            val LATITUDE = "latitude"
            val LONGITUDE = "longitude"
        }
    }
}
