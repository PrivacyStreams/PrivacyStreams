package io.github.privacystreams.app

import io.github.privacystreams.device.DeviceState
import io.github.privacystreams.location.Geolocation

object Config {
    val APP_NAME = "PStream"
    val DATA_DIR = APP_NAME + "/data"

    val USE_WAKELOCK = false

    var COLLECT_LOCATION_INTERVAL = (1 * 60 * 1000).toLong()
    var COLLECT_LOCATION_LEVEL = Geolocation.LEVEL_EXACT
    var COLLECT_DEVICE_STATE_INTERVAL = (1 * 60 * 1000).toLong()
    var COLLECT_DEVICE_STATE_MASK = DeviceState.Masks.SCREEN_STATE or DeviceState.Masks.BATTERY_LEVEL or DeviceState.Masks.CONNECTION_INFO
    var COLLECT_AUDIO_INTERVAL = (5 * 60 * 1000).toLong()
    var COLLECT_AUDIO_DURATION = (10 * 1000).toLong()
    var COLLECT_IMAGE_INTERVAL = (5 * 60 * 1000).toLong()
    var COLLECT_IMAGE_CAMERA_ID = 0
}
