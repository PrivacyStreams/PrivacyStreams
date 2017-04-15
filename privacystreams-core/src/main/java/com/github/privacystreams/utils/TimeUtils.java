package com.github.privacystreams.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A helper class to access timestamp-related utilities.
 */

public class TimeUtils {
    /**
     * Get timestamp from a formatted time string, e.g. 2016-08-12, 2016-08-12 09:00:15.000
     *
     * @param timeFormat the format string of time
     * @param timeString the time string
     * @return the timestamp value of the given time string
     */
    public static Long fromFormattedString(String timeFormat, String timeString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());
            Date parsedDate = dateFormat.parse(timeString);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Get current timestamp.
     */
    public static Long now() {
        return System.currentTimeMillis();
    }

    /**
     * Generate a time tag of current timestamp, using the time format at `Globals.TimeConfig.defaultTimeFormat`.
     * @return the formatted string
     */
    public static String getTimeTag() {
        return toFormattedString(now());
    }

    /**
     * Format a timestamp to a string using the time format at `Globals.TimeConfig.defaultTimeFormat`.
     * @param timestamp the timestamp to format
     * @return the formatted string
     */
    public static String toFormattedString(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat tf = new SimpleDateFormat(Globals.TimeConfig.defaultTimeFormat, Locale.getDefault());
        return tf.format(date);
    }

    /**
     * Format a timestamp to a string using a time format.
     *
     * @param timeFormat the time format
     * @param timestamp the timestamp to format
     * @return the formatted string
     */
    public static String toFormattedString(String timeFormat, long timestamp) {
        if (timeFormat == null) timeFormat = Globals.TimeConfig.defaultTimeFormat;
        Date date = new Date(timestamp);
        SimpleDateFormat tf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        return tf.format(date);
    }
}
