package io.github.privacystreams.commons.time;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.UQI;

/**
 * Generate a time tag string
 */
final class CurrentTimeGetter extends Function<Void, Long> {
    CurrentTimeGetter() {
    }

    @Override
    public Long apply(UQI uqi, Void input) {
        return System.currentTimeMillis();
    }
}
