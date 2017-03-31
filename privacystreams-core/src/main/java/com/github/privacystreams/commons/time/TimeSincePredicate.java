package com.github.privacystreams.commons.time;

/**
 * Check whether the timestamp specified by a field is since a given timestamp.
 */
final class TimeSincePredicate extends TimeProcessor<Boolean> {
    private final long timestampToCompare;

    TimeSincePredicate(final String timestampField, final long timestampToCompare) {
        super(timestampField);
        this.timestampToCompare = timestampToCompare;
        this.addParameters(timestampToCompare);
    }

    @Override
    protected Boolean processTimestamp(long timestamp) {
        return timestamp >= this.timestampToCompare;
    }
}
