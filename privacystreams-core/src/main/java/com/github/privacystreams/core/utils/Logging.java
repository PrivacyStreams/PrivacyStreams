package com.github.privacystreams.core.utils;

import android.util.Log;

/**
 * Created by yuanchun on 05/11/2016.
 * Logging utils
 */

public class Logging {
    private static boolean isEnabled = true;
    public static void enableLogging() {
        Logging.isEnabled = true;
    }
    public static void disableLogging() {
        Logging.isEnabled = false;
    }

    private static int level = Log.DEBUG;
    public static void setLevel(int level) {
        Logging.level = level;
    }

    public static void debug(String message) {
        if (Logging.isEnabled && Logging.level <= Log.DEBUG) {
            Log.d(Consts.LIB_TAG, message);
        }
    }

    public static void warn(String message) {
        if (Logging.isEnabled && Logging.level <= Log.WARN) {
            Log.w(Consts.LIB_TAG, message);
        }
    }
}
