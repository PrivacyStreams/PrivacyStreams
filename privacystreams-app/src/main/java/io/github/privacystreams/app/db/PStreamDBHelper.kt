package io.github.privacystreams.app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose


abstract class PStreamDBHelper(context: Context, itemClass: Class<*>) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val tableName: String = itemClass.simpleName
    protected val uqi: UQI = UQI(context)
    protected val purpose: Purpose = Purpose.TEST(tableName)

    var isCollecting: Boolean = false

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

    protected abstract val sqlCreateEntries: String

    protected val sqlDeleteEntries: String
        get() = "DROP TABLE IF EXISTS " + tableName

    fun startCollecting() {
        this.uqi.stopAll()
        this.redirectPrivacyStreamsToDB()
        this.isCollecting = true
    }

    fun stopCollecting() {
        this.uqi.stopAll()
        this.isCollecting = false
    }

    protected abstract fun redirectPrivacyStreamsToDB()

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "privacystreams.db"
    }
}
