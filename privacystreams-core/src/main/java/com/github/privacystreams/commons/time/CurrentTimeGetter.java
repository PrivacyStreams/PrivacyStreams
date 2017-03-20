package com.github.privacystreams.commons.time;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

/**
 * Generate a time tag string
 */
final class CurrentTimeGetter<Tin> extends Function<Tin, Long> {
    CurrentTimeGetter() {
    }

    @Override
    public Long apply(UQI uqi, Tin input) {
        return System.currentTimeMillis();
    }
}
