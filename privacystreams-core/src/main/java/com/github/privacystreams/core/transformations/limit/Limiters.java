package com.github.privacystreams.core.transformations.limit;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 30/12/2016.
 * A helper class to access stream-limiting functions
 */
public class Limiters {
    /**
     * A function that limit the stream with a timeout.
     *
     * @param timeoutMillis the timeout time, in milliseconds.
     * @return the stream-limiting function.
     */
    public static Function<MultiItemStream, MultiItemStream> timeout(long timeoutMillis) {
        return new TimeoutLimiter(timeoutMillis);
    }

    /**
     * A function that limit the stream to at most n items.
     *
     * @param countLimit the count of items at most.
     * @return the stream-limiting function.
     */
    public static Function<MultiItemStream, MultiItemStream> limitCount(int countLimit) {
        return new CountLimiter(countLimit);
    }

    /**
     * A function that limit the stream with a predicate.
     * The stream will stop if the predicate is dissatisfied.
     *
     * @param predicate the predicate to check for each item.
     * @return the stream-limiting function.
     */
    public static Function<MultiItemStream, MultiItemStream> limit(Function<Item, Boolean> predicate) {
        return new PredicateLimiter(predicate);
    }

}
