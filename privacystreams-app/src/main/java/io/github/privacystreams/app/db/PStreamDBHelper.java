package io.github.privacystreams.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;


public abstract class PStreamDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "privacystreams.db";

    protected final String tableName;
    protected final UQI uqi;
    protected final Purpose purpose;

    public boolean isCollecting;

    public PStreamDBHelper(Context context, Class itemClass) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        tableName = itemClass.getSimpleName();
        uqi = new UQI(context);
        purpose = Purpose.TEST(tableName);
        isCollecting = false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getSqlCreateEntries());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(getSqlDeleteEntries());
        onCreate(db);
    }

    protected abstract String getSqlCreateEntries();

    protected String getSqlDeleteEntries() {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public final void startCollecting() {
        this.uqi.stopAll();
        this.redirectPrivacyStreamsToDB();
        this.isCollecting = true;
    }

    public final void stopCollecting() {
        this.uqi.stopAll();
        this.isCollecting = false;
    }

    protected abstract void redirectPrivacyStreamsToDB();
}
