package io.github.privacystreams.app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose
import java.util.*


abstract class PStreamDBHelper(context: Context, itemClass: Class<*>)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val tableName: String = itemClass.simpleName
    protected abstract val sqlCreateEntries: String
    protected val sqlDeleteEntries = "DROP TABLE IF EXISTS " + tableName
    abstract val iconResId: Int
    abstract val dbStatus: PStreamDBStatus

    protected val uqi: UQI = UQI(context)
    protected val purpose: Purpose = Purpose.TEST(tableName)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateEntries)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(sqlDeleteEntries)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        this.onUpgrade(db, oldVersion, newVersion)
    }

    fun initStatus() {
        try {
            val cur = readableDatabase.query(this.tableName, null, null, null, null, null, null)
            this.dbStatus.numItems.set(cur.count)
            cur.close()
        } catch (ignored: Throwable) {
        }
    }

    fun startCollecting() {
        this.dbStatus.isCollecting.set(true)
        this.uqi.stopAll()
        this.redirectPrivacyStreamsToDB()
    }

    fun stopCollecting() {
        this.dbStatus.isCollecting.set(false)
        this.uqi.stopAll()
    }

    protected abstract fun redirectPrivacyStreamsToDB()

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "privacystreams.db"

        fun getAllDBHelpers(context: Context): List<PStreamDBHelper> {
            val dbHelpers: MutableList<PStreamDBHelper> = ArrayList()
            dbHelpers.add(PSLocationDBHelper(context))
            dbHelpers.add(PSNotificationDBHelper(context))
            return dbHelpers
        }
    }

}
