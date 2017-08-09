package io.github.privacystreams.app.db

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import io.github.privacystreams.app.NavActivity
import io.github.privacystreams.app.R

/**
 * The PrivacyStreams always-on service for collecting historic data.
 */

class PStreamCollectService : Service() {
    companion object {
        val START_TABLE_NAME_KEY = "start_table_name"
        val STOP_TABLE_NAME_KEY = "stop_table_name"
        val ALL_TABLES = "all_tables"
        private val ONGOING_NOTIFICATION_ID = 1
    }

    internal lateinit var dbHelper: PStreamDBHelper
    internal lateinit var dbTables: List<PStreamTable>

    override fun onCreate() {
        dbHelper = PStreamDBHelper.getInstance(this)
        dbTables = dbHelper.tables

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
        dbTables.forEach { it.stopCollecting() }
        stopForeground(true)
        dbHelper.close()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val stopTableName = intent.getStringExtra(STOP_TABLE_NAME_KEY)
        if (stopTableName == ALL_TABLES) {
            dbTables.forEach { it.stopCollecting() }
        } else if (stopTableName != null) {
            dbTables.forEach { if (it.tableName == stopTableName) it.stopCollecting() }
        }

        val startTableName = intent.getStringExtra(START_TABLE_NAME_KEY)
        if (startTableName == ALL_TABLES) {
            dbTables.forEach { it.startCollecting() }
        } else if (startTableName != null) {
            dbTables.forEach { if (it.tableName == startTableName) it.startCollecting() }
        }

        var allTablesStopped = true
        dbTables.forEach { if (it.isCollecting.get()) allTablesStopped = false }

        if (allTablesStopped) this.stopSelf()

        return Service.START_STICKY
    }

}
