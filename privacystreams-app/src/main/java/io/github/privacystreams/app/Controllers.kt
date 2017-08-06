package io.github.privacystreams.app


import android.content.Context
import android.content.Intent
import io.github.privacystreams.app.db.PStreamDBStatus
import io.github.privacystreams.app.providers.PStreamHistory
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose
import java.util.*

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
        UQI(context).getData(PStreamHistory.locations(), Purpose.TEST("Test location history"))
                .debug()
//        PStreamDBStatus.v().changeMessage()
    }
}
