package io.github.privacystreams.app.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose
import java.util.*

class PStreamDBHelper private constructor(var context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_VERSION = 2
        val DB_NAME = "privacystreams.db"

        @SuppressLint("StaticFieldLeak")
        private var instance: PStreamDBHelper? = null
        fun getInstance(context: Context): PStreamDBHelper {
            if (instance == null) {
                instance = PStreamDBHelper(context.applicationContext)
            }
            else {
                instance!!.context = context.applicationContext
            }
            return instance!!
        }
    }

    val tables: List<PStreamTable> = this.getAllTables()

    private fun getAllTables(): List<PStreamTable> {
        val tables: MutableList<PStreamTable> = ArrayList<PStreamTable>()
        tables.add(TableGeolocation(this))
        tables.add(TableNotification(this))
        tables.add(TableUIEvent(this))
        tables.add(TableDeviceEvent(this))
        tables.add(TableDeviceState(this))
        tables.add(TableBgPhoto(this))
        tables.add(TableBgAudio(this))
        return tables
    }

    override fun onCreate(db: SQLiteDatabase) {
        tables.map { it.sqlCreateEntries.map{db.execSQL(it)} }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        tables.map { it.sqlDeleteEntries.map{db.execSQL(it)} }
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        this.onUpgrade(db, oldVersion, newVersion)
    }

    fun startCollectServiceAll() = PStreamCollectService.startAllTables(context)

    fun stopCollectServiceAll() = PStreamCollectService.stopAllTables(context)

    fun test() {
        UQI(context).getData(TableGeolocation.PROVIDER(), Purpose.TEST("Test"))
                .limit(10)
                .debug();

        UQI(context).getData(TableNotification.PROVIDER(), Purpose.TEST("Test"))
                .limit(10)
                .debug();
    }

    fun getDBSize(): Long {
        return context.getDatabasePath(DB_NAME).absoluteFile.length()
    }

    fun backupData() {
        // TODO backup the database
    }

    fun restoreData() {
        // TODO restore the database
    }

    fun clearData() {
        stopCollectServiceAll()
        onUpgrade(writableDatabase, DB_VERSION, DB_VERSION)
        tables.forEach { it.initStatus() }
    }
}
