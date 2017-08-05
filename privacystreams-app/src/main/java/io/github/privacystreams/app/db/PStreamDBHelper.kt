package io.github.privacystreams.app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.graphics.drawable.Drawable

import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.purposes.Purpose
import java.util.*


abstract class PStreamDBHelper(context: Context, itemClass: Class<*>) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val tableName: String = itemClass.simpleName
    abstract val iconResId: Int

    protected val uqi: UQI = UQI(context)
    protected val purpose: Purpose = Purpose.TEST(tableName)

    val isCollecting: ObservableBoolean = ObservableBoolean(false)
    val numItems: ObservableInt = ObservableInt(0)

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
        this.isCollecting.set(true)

        val db = this.readableDatabase
        val cur = db.query(this.tableName, null, null, null, null, null, null)
        this.numItems.set(cur.count)
        cur.close()

        this.uqi.stopAll()
        this.redirectPrivacyStreamsToDB()
    }

    fun stopCollecting() {
        this.isCollecting.set(false)

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
            return dbHelpers
        }
    }
}
