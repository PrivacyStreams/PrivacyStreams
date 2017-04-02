package com.github.privacystreams.commons.time;

import com.github.privacystreams.utils.TimeUtils;

/**
 * Generate a time tag string
 */
final class TimeToStringConverter extends TimeProcessor<String> {

    private final String timeFormat;

    TimeToStringConverter(final String timestampField, final String timeFormat) {
        super(timestampField);
        this.timeFormat = timeFormat;
        this.addParameters(timeFormat);
    }

    @Override
    protected String processTimestamp(long timestamp) {
        return TimeUtils.toFormattedString(this.timeFormat, timestamp);
    }
}
