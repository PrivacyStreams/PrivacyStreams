package io.github.privacystreams.app.db

import io.github.privacystreams.core.PStreamProvider
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose


abstract class PStreamTable(val dbHelper: PStreamDBHelper): PStreamProvider() {

    val tableName: String = this.javaClass.simpleName
    abstract val sqlCreateEntry: String
    val sqlDeleteEntry = "DROP TABLE IF EXISTS " + tableName
    abstract val iconResId: Int
    abstract val tableStatus: PStreamTableStatus

    protected val uqi: UQI = UQI(dbHelper.context)
    protected val purpose: Purpose = Purpose.TEST(tableName)

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
        this.uqi.stopAll()
        this.collectStreamToTable()
    }

    fun stopCollecting() {
        this.tableStatus.isCollecting.set(false)
        this.uqi.stopAll()
    }

    abstract fun collectStreamToTable()

}
