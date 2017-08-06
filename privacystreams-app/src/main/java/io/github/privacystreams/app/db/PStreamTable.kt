package io.github.privacystreams.app.db

import io.github.privacystreams.core.PStreamProvider
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose


abstract class PStreamTable(val dbHelper: PStreamDBHelper) {

    abstract val tableName: String
    abstract val sqlCreateEntry: String
    abstract val sqlDeleteEntry: String
    abstract val iconResId: Int
    abstract val tableStatus: PStreamTableStatus

    protected val uqi: UQI = UQI(dbHelper.context)
    protected val purpose: Purpose = Purpose.TEST("Save data to DB for future use")

    fun initStatus() {
        try {
            val cur = dbHelper.readableDatabase.query(this.tableName,
                    null, null, null, null, null, null)
            this.tableStatus.numItems.set(cur.count)
            cur.close()
        } catch (ignored: Throwable) {
        }
    }

    fun startCollecting() {
        this.tableStatus.isCollecting.set(true)
        this.tableStatus.message.set("")
        this.uqi.stopAll()
        this.collectStreamToTable()
    }

    fun stopCollecting() {
        this.tableStatus.isCollecting.set(false)
        this.tableStatus.message.set("")
        this.uqi.stopAll()
    }

    abstract fun collectStreamToTable()

}
