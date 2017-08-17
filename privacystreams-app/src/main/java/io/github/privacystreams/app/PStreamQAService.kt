package io.github.privacystreams.app

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * The PrivacyStreams remote query-answering service.
 */

class PStreamQAService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
