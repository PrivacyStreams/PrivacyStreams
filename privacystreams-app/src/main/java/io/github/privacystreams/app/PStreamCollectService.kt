package io.github.privacystreams.app

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

import io.github.privacystreams.app.db.PStreamDBHelper

/**
 * The PrivacyStreams always-on service for collecting historic data.
 */

class PStreamCollectService : Service() {

    internal val dbHelper = PStreamDBHelper.getInstance(this)
    internal val dbTables = dbHelper.tables

    override fun onCreate() {
        dbTables.map { it.startCollecting() }

        val notificationIntent = Intent(this, NavActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(NavActivity.NAV_ID_KEY, R.id.nav_data)
        notificationIntent.putExtras(bundle)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

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
        dbTables.map { it.stopCollecting() }
        stopForeground(true)
        dbHelper.close()
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
