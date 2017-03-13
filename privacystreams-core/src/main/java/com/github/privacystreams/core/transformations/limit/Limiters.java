package com.github.privacystreams.core.transformations.limit;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access stream-limiting functions
 */
@PSOperatorWrapper
public class Limiters {
    /**
     * Limit the stream with a timeout.
     * The stream will stop after the timeout.
     *
     * @param timeoutMillis the timeout milliseconds.
     * @return the limiter function.
     */
    public static Function<MStream, MStream> timeout(long timeoutMillis) {
        return new TimeoutLimiter(timeoutMillis);
    }

    /**
     * Limit the stream to at most n items.
     * The stream will stop after n items.
     *
     * @param countLimit the maximum number of items.
     * @return the limiter function.
     */
    public static Function<MStream, MStream> limitCount(int countLimit) {
        return new CountLimiter(countLimit);
    }

    /**
     * Limit the stream with a predicate.
     * The stream will stop if the predicate is dissatisfied.
     *
     * @param predicate the predicate to check for each item.
     * @return the limiter function.
     */
    public static Function<MStream, MStream> limit(Function<Item, Boolean> predicate) {
        return new PredicateLimiter(predicate);
    }

}
