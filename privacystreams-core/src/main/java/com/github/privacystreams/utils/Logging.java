package com.github.privacystreams.utils;

import android.util.Log;

/**
 * A helper class to get access to log-related utilities.
 */

public class Logging {

    public static void debug(String message) {
        if (Globals.LoggingConfig.isEnabled && Globals.LoggingConfig.level <= Log.DEBUG) {
            Log.d(Consts.LIB_TAG, message);
        }
    }

    public static void warn(String message) {
        if (Globals.LoggingConfig.isEnabled && Globals.LoggingConfig.level <= Log.WARN) {
            Log.w(Consts.LIB_TAG, message);
        }
    }

    public static void error(String message) {
        if (Globals.LoggingConfig.isEnabled && Globals.LoggingConfig.level <= Log.ERROR) {
            Log.e(Consts.LIB_TAG, message);
        }
    }
}
