package io.github.privacystreams.app

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder

import io.github.privacystreams.app.db.PStreamDBHelper
import io.github.privacystreams.app.db.PStreamTable

/**
 * The PrivacyStreams always-on service for collecting historic data.
 */

class PStreamCollectService : Service() {

    internal val dbHelper = PStreamDBHelper(this)

    override fun onCreate() {
        dbHelper.tables.map { it.startCollecting() }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = Notification.Builder(this)
                .setContentTitle(getText(R.string.collect_notification_title))
                .setContentText(getText(R.string.collect_notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.collect_notification_title))
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        dbHelper.tables.map { it.stopCollecting() }
        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    companion object {
        private val ONGOING_NOTIFICATION_ID = 1
    }
}
