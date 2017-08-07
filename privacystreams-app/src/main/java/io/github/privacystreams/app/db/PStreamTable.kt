package io.github.privacystreams.app.db

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import io.github.privacystreams.core.PStreamProvider
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose


abstract class PStreamTable(val dbHelper: PStreamDBHelper) {

    abstract val tableName: String
    abstract val sqlCreateEntry: String
    abstract val sqlDeleteEntry: String
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

    fun startCollecting() {
        this.isCollecting.set(true)
        this.message.set("")
        this.uqi.stopAll()
        this.collectStreamToTable()
    }

    fun stopCollecting() {
        this.isCollecting.set(false)
        this.message.set("")
        this.uqi.stopAll()
    }

    protected fun increaseNumItems() {
        numItems.set(numItems.get() + 1)
    }

    abstract fun collectStreamToTable()

}
