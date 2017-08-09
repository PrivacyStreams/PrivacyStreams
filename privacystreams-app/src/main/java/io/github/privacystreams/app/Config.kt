package io.github.privacystreams.app

import io.github.privacystreams.device.DeviceState
import io.github.privacystreams.location.Geolocation

object Config {
    var COLLECT_LOCATION_INTERVAL = (1000 * 10).toLong()
    var COLLECT_LOCATION_LEVEL = Geolocation.LEVEL_EXACT
    var COLLECT_DEVICE_STATE_INTERVAL = (1000 * 10).toLong()
    var COLLECT_DEVICE_STATE_MASK = DeviceState.Masks.SCREEN_STATE or DeviceState.Masks.BATTERY_LEVEL or DeviceState.Masks.CONNECTION_INFO
}
