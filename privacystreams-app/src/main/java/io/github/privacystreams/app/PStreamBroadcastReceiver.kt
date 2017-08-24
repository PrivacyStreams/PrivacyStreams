package io.github.privacystreams.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.privacystreams.app.db.PStreamCollectService

/**
 * Receive broadcasts and react
 */
class PStreamBroadcastReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Config.APP_NAME, "Broadcast received!")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start services when boot completed
            PStreamCollectService.start(context)
        }
    }
}
