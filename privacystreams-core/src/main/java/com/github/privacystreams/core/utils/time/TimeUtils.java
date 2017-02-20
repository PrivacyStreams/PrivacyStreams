package com.github.privacystreams.core.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yuanchun on 08/12/2016.
 * A helper class to get timestamp value
 */

public class TimeUtils {
    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault());

    // get timestamp from a date string, e.g. 2016-08-12
    public static Long fromDateString(String dateString) {
        try {
            String timeString = dateString + " 00:00:00.000";
            Date parsedDate = defaultDateFormat.parse(timeString);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // get timestamp from a time string, e.g. 2016-08-12 09:00:15.000
    public static Long fromTimeString(String timeString) {
        try {
            Date parsedDate = defaultDateFormat.parse(timeString);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get timestamp from a formatted time string, e.g. 2016-08-12, 2016-08-12 09:00:15.000
     * @param timeFormat the format string of time
     * @param timeString the time string
     * @return the timestamp value of the given time string
     */
    public static Long format(String timeFormat, String timeString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());
            Date parsedDate = dateFormat.parse(timeString);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // get asLastKnown timestamp
    public static Long now() {
        return System.currentTimeMillis();
    }
}
