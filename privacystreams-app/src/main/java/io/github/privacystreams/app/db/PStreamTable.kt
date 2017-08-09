package io.github.privacystreams.app.db

import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose


abstract class PStreamTable(val dbHelper: PStreamDBHelper) {

    abstract val tableName: String
    abstract val sqlCreateEntries: List<String>
    abstract val sqlDeleteEntries: List<String>
    abstract val iconResId: Int

    val message = ObservableField<String>("")
    val isCollecting = ObservableBoolean(false)
    val numItems = ObservableInt(0)

    protected val uqi: UQI = UQI(dbHelper.context)
    protected val purpose: Purpose = Purpose.TEST("Save data to DB for future use")

    fun initStatus() {
        try {
            val cur = dbHelper.readableDatabase.query(this.tableName,
                    null, null, null, null, null, null)
            this.numItems.set(cur.count)
            cur.close()
        } catch (ignored: Throwable) {
        }
    }

    fun startCollectService() {
        if (isCollecting.get()) return
        val collectServiceIntent = Intent(dbHelper.context, PStreamCollectService::class.java)
        collectServiceIntent.putExtra(PStreamCollectService.START_TABLE_NAME_KEY, tableName)
        dbHelper.context.startService(collectServiceIntent)
    }

    fun stopCollectService() {
        if (!isCollecting.get()) return
        val collectServiceIntent = Intent(dbHelper.context, PStreamCollectService::class.java)
        collectServiceIntent.putExtra(PStreamCollectService.STOP_TABLE_NAME_KEY, tableName)
        dbHelper.context.startService(collectServiceIntent)
    }

    fun startCollecting() {
        if (isCollecting.get()) return
        this.message.set("")
        this.uqi.stopAll()
        this.isCollecting.set(true)
        this.collectStreamToTable()
    }

    fun stopCollecting() {
        if (!isCollecting.get()) return
        this.message.set("")
        this.uqi.stopAll()
        this.isCollecting.set(false)
    }

    protected fun increaseNumItems() {
        numItems.set(numItems.get() + 1)
    }

    abstract fun collectStreamToTable()

}
