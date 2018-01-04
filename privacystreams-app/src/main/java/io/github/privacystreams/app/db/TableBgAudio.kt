package io.github.privacystreams.app.db

import android.Manifest
import android.content.ContentValues
import android.util.Log
import io.github.privacystreams.app.Config
import io.github.privacystreams.app.R
import io.github.privacystreams.audio.Audio
import io.github.privacystreams.audio.AudioOperators
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.utils.StorageUtils
import io.github.privacystreams.utils.TimeUtils
import java.io.File

class TableBgAudio(dbHelper: PStreamDBHelper) : PStreamTable(dbHelper) {

    companion object {
        val TABLE_NAME = "BgAudio"
        val ICON_RES_ID = R.drawable.microphone;

        /* Fields */
        val _ID = "_id"                                 // Long
        val TIME_CREATED = Audio.TIME_CREATED           // Long
        val AUDIO_PATH = "audio_path"                   // Long
    }

    override val tableName: String = TABLE_NAME
    override val iconResId: Int = ICON_RES_ID

    override val sqlCreateEntries = listOf<String>(
            "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$TIME_CREATED INTEGER," +
                "$AUDIO_PATH TEXT)",
            "CREATE INDEX ${TABLE_NAME}_time_created_index on $TABLE_NAME ($TIME_CREATED)"
    )

    override val sqlDeleteEntries = listOf<String>(
            "DROP TABLE IF EXISTS $TABLE_NAME"
    )

    override fun collectStreamToTable() {
        val db = dbHelper.writableDatabase
        this.uqi.getData(Audio.recordPeriodic(Config.COLLECT_AUDIO_DURATION, Config.COLLECT_AUDIO_INTERVAL), this.purpose)
                .setField("tempPath", AudioOperators.getFilepath(Audio.AUDIO_DATA))
                .logAs(this.tableName)
                .forEach(object : Callback<Item>() {
                    init {
                        addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }

                    override fun onInput(input: Item) {
                        val values = ContentValues()

                        try {
                            val tempPath : String = input.getValueByField("tempPath")
                            val tempFile = File(tempPath)
                            val audioPath = Config.DATA_DIR + "/audio_" + TimeUtils.getTimeTag() + ".amr"
                            val audioFile : File = StorageUtils.getValidFile(uqi.context, audioPath, true)
                            tempFile.copyTo(audioFile, true)
                            tempFile.delete()
                            values.put(AUDIO_PATH, audioFile.absolutePath)
                        } catch (e: Exception) {
                            Log.e(TABLE_NAME, "fail to write audio")
                            e.printStackTrace()
                        }

                        values.put(TIME_CREATED, input.getAsLong(Audio.TIME_CREATED))
                        db.insert(tableName, null, values)
                        increaseNumItems()
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
                item.setFieldValue(AUDIO_PATH, cur.getString(cur.getColumnIndex(AUDIO_PATH)))
                output(item)
            }
            cur.close()
        }
    }
}