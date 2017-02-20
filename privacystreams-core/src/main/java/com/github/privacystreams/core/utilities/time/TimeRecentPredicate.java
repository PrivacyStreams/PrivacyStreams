package com.github.privacystreams.core.utilities.time;

import java.util.List;

import com.github.privacystreams.core.utils.time.TimeUtils;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that return true if a timestamp value is recent from now
 */
final class TimeRecentPredicate extends TimeProcessor<Boolean> {

    private final long duration;

    TimeRecentPredicate(final String timestampField, final long duration) {
        super(timestampField);
        this.duration = duration;
    }

    @Override
    protected Boolean processTimestamp(long timestamp) {
        return timestamp >= TimeUtils.now() - this.duration;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = super.getParameters();
        parameters.add(String.valueOf(this.duration));
        return parameters;
    }
}
