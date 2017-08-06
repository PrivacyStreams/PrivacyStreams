package io.github.privacystreams.app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class PStreamDBHelper(val context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_VERSION = 2
        val DB_NAME = "privacystreams.db"
    }

    val tables: List<PStreamTable>
        get() {
            val tables: MutableList<PStreamTable> = ArrayList()
            tables.add(PSGeolocationTable(this))
            tables.add(PSNotificationTable(this))
            return tables
        }

    override fun onCreate(db: SQLiteDatabase) {
        tables.map { db.execSQL(it.sqlCreateEntry) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        tables.map { db.execSQL(it.sqlDeleteEntry) }
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        this.onUpgrade(db, oldVersion, newVersion)
    }
}
