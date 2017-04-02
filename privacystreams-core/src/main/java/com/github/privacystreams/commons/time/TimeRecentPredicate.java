package com.github.privacystreams.commons.time;

import com.github.privacystreams.utils.TimeUtils;

/**
 * Check whether the timestamp specified by a field is recent from now.
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
