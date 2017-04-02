package com.github.privacystreams.utils;

/**
 * A helper class to get millisecond duration time.
 */

public class Duration {
    public static Long milliseconds(long n) {
        return n;
    }

    public static Long seconds(long n) {
        return n * 1000L;
    }

    public static Long minutes(long n) {
        return n * 60 * 1000L;
    }

    public static Long hours(long n) {
        return n * 60 * 60 * 1000L;
    }

    public static Long days(long n) {
        return n * 24 * 60 * 60 * 1000L;
    }
}
