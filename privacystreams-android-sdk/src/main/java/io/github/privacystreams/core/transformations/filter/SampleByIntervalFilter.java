package io.github.privacystreams.core.transformations.filter;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;

import static io.github.privacystreams.utils.Assertions.notNull;

/**
 * Sample the items in the stream.
 */
final class SampleByIntervalFilter extends StreamFilter {
    private final long minInterval;

    SampleByIntervalFilter(long minInterval) {
        this.minInterval = minInterval;
        this.addParameters(minInterval);
    }

    private transient long lastItemTime = 0;

    @Override
    public boolean keep(Item item) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastItemTime < minInterval) return false;
        lastItemTime = currentTime;
        return true;
    }

}
