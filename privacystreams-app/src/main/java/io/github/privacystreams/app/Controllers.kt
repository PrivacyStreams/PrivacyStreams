package io.github.privacystreams.app


import android.content.Context
import android.content.Intent
import io.github.privacystreams.app.db.PSGeolocationTable
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose

class Controllers internal constructor(private val context: Context) {

    fun startCollectService() {
        val collectServiceIntent = Intent(this.context, PStreamCollectService::class.java)
        this.context.startService(collectServiceIntent)
    }

    fun stopCollectService() {
        val collectServiceIntent = Intent(this.context, PStreamCollectService::class.java)
        this.context.stopService(collectServiceIntent)
    }

    fun test() {
//        PStreamTableStatus.v().changeMessage()
    }
}
