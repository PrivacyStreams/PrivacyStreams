package io.github.privacystreams.app


import android.content.Context
import android.content.Intent

import io.github.privacystreams.core.actions.collect.Collectors

class Controllers internal constructor(private val context: Context) {

    fun startCollectService() {
        val collectServiceIntent = Intent(this.context, PStreamCollectService::class.java)
        this.context.startService(collectServiceIntent)
    }

    fun stopCollectService() {
        val collectServiceIntent = Intent(this.context, PStreamCollectService::class.java)
        this.context.stopService(collectServiceIntent)
    }
}
