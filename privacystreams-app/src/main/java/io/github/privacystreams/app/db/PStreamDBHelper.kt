package io.github.privacystreams.app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class PStreamDBHelper private constructor(var context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_VERSION = 2
        val DB_NAME = "privacystreams.db"

        private var instance: PStreamDBHelper? = null
        fun getInstance(context: Context): PStreamDBHelper {
            if (instance == null) {
                instance = PStreamDBHelper(context)
            }
            else {
                instance!!.context = context
            }
            return instance!!
        }
    }

    val tables: List<PStreamTable> = this.getAllTables()

    private fun getAllTables(): List<PStreamTable> {
        val tables: MutableList<PStreamTable> = ArrayList<PStreamTable>()
        tables.add(PSGeolocationTable(this))
        tables.add(PSNotificationTable(this))
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
}
