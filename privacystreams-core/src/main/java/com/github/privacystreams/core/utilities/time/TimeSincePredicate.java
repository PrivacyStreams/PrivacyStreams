package com.github.privacystreams.core.utilities.time;

import java.util.List;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that return true if a timestamp value is since a given timestamp
 */
final class TimeSincePredicate extends TimeProcessor<Boolean> {
    private final long timestampToCompare;

    TimeSincePredicate(final String timestampField, final long timestampToCompare) {
        super(timestampField);
        this.timestampToCompare = timestampToCompare;
    }

    @Override
    protected Boolean processTimestamp(long timestamp) {
        return timestamp >= this.timestampToCompare;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = super.getParameters();
        parameters.add(String.valueOf(this.timestampToCompare));
        return parameters;
    }
}
