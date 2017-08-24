package io.github.privacystreams.utils;

import android.media.MediaRecorder;
import android.util.Log;

/**
 * Global configurations for PrivacyStreams.
 */

public class Globals {
    public static class LocationConfig {
        public static boolean useGoogleService = true;
    }

    public static class AudioConfig {
        public static int outputFormat = MediaRecorder.OutputFormat.AMR_NB;
        public static int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;
        public static int audioSource = MediaRecorder.AudioSource.MIC;
    }

    public static class DebugConfig {
        public static int socketPort = 7336;
    }

    public static class LoggingConfig {
        public static boolean isEnabled = true;
        public static int level = Log.DEBUG;
    }

    public static class EmailConfig {
        public static long pollingInterval = Duration.minutes(2);
        public static int defaultMaxNumberOfReturnResults = Integer.MAX_VALUE;
    }

    public static class DriveConfig {
        public static long pollingInterval = Duration.minutes(2);
        public static int defaultMaxNumberOfReturnDrives = Integer.MAX_VALUE;
    }

    public static class HashConfig {
        public static String defaultAlgorithm = HashUtils.SHA256;

    }

    public static class TimeConfig {
        public static String defaultTimeFormat = "yyyyMMdd_HHmmssSSS";

    }

    public static class DropboxConfig {
        public static String accessToken = "";
        public static long leastSyncInterval = 0;
        public static boolean onlyOverWifi = false;
    }

    public static class StorageConfig {
        public static String fileAppendSeparator = "\n\n\n";
    }
}
