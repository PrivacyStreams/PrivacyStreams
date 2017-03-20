package com.github.privacystreams.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Global configuration for PrivacyStreams
 */

public class GlobalConfig {
    public static class LoggingConfig {
        public static boolean isEnabled = true;
        public static int level = Log.DEBUG;
    }

    public static class HashConfig {
        public static String defaultHashAlgorithm = HashUtils.SHA256;

    }

    public static class TimeConfig {
        public static String defaultTimeFormat = "yyyyMMdd_HHmmss_SSS";

    }

    public static class DropboxConfig {
        public static String accessToken = "";
        public static long leastSyncInterval = 0;
        public static boolean onlyOverWifi = false;
    }
}
