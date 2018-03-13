package io.github.privacystreams.app.db

import android.Manifest
import android.content.ContentValues
import android.util.Log
import io.github.privacystreams.app.Config
import io.github.privacystreams.app.R
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.image.Image
import io.github.privacystreams.image.ImageOperators
import io.github.privacystreams.notification.Notification
import io.github.privacystreams.utils.StorageUtils
import io.github.privacystreams.utils.TimeUtils
import java.io.File
import javax.security.auth.Subject

class TableKnowledge(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "Knowledge"
        val ICON_RES_ID = R.drawable.knowledge;

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = Notification.TIME_CREATED    // Long
        val SUBJECT = "subject"                         // String
        val OBJECT = "object"                           // String
        val PREDICATE = "predicate"                     // String
        val PACKAGE_NAME = "package_name"               // String
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$SUBJECT TEXT," +
                "$PREDICATE TEXT," +
                "$OBJECT TEXT," +
                "$PACKAGE_NAME TEXT)",
            "CREATE INDEX ${TABLE_NAME}_${TIME_CREATED}_index on $TABLE_NAME ($TIME_CREATED)",
            "CREATE INDEX ${TABLE_NAME}_subject_index on $TABLE_NAME ($SUBJECT)",
            "CREATE INDEX ${TABLE_NAME}_predicate_index on $TABLE_NAME ($PREDICATE)",
            "CREATE INDEX ${TABLE_NAME}_object_index on $TABLE_NAME ($OBJECT)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(Image.takePhotoBgPeriodic(Config.COLLECT_IMAGE_CAMERA_ID, Config.COLLECT_IMAGE_INTERVAL), this.purpose)
                .setField("tempPath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    init {
                        addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }

                    override fun onInput(input: Item) {
                        // TODO implement this
                    }

                    override fun onFail(exception: PSException) {
                        stopCollectService()
                        if (exception.isPermissionDenied) {
                            message.set("Denied")
                        }
                    }
                })
    }

    class PROVIDER(): PStreamTableProvider() {
        override fun provide() {
            val dbHelper = PStreamDBHelper.getInstance(context)
            val db = dbHelper.readableDatabase
            val cur = db.query(TABLE_NAME, null, null, null, null, null, null)
            while (cur.moveToNext()) {
                val item = Item()
                item.setFieldValue(TIME_CREATED, cur.getLong(cur.getColumnIndex(TIME_CREATED)))
                item.setFieldValue(SUBJECT, cur.getString(cur.getColumnIndex(SUBJECT)))
                item.setFieldValue(PREDICATE, cur.getString(cur.getColumnIndex(PREDICATE)))
                item.setFieldValue(OBJECT, cur.getString(cur.getColumnIndex(OBJECT)))
                item.setFieldValue(PACKAGE_NAME, cur.getString(cur.getColumnIndex(PACKAGE_NAME)))

                output(item)
            }
            cur.close()
        }
    }
}
