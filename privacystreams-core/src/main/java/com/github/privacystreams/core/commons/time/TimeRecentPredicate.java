package com.github.privacystreams.core.commons.time;

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
        this.addParameters(duration);
    }

    @Override
    protected Boolean processTimestamp(long timestamp) {
        return timestamp >= TimeUtils.now() - this.duration;
    }
}
