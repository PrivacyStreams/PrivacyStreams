package io.github.privacystreams.app

import io.github.privacystreams.location.Geolocation

object Config {
    var COLLECT_LOCATION_INTERVAL = (1000 * 10).toLong()
    var COLLECT_LOCATION_LEVEL = Geolocation.LEVEL_EXACT
}
