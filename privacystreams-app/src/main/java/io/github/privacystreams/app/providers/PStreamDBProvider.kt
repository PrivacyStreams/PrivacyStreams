package io.github.privacystreams.app.providers

import android.content.ContentValues
import io.github.privacystreams.app.db.PStreamContract
import io.github.privacystreams.app.db.PStreamDBHelper
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.PStreamProvider
import io.github.privacystreams.location.Geolocation
import io.github.privacystreams.location.LatLon
import io.github.privacystreams.app.db.PStreamContract.GeolocationEntry

/**
 * Provide historic data from database.
 */
abstract class PStreamDBProvider() : PStreamProvider() {
    abstract val dbHelper: PStreamDBHelper
}
