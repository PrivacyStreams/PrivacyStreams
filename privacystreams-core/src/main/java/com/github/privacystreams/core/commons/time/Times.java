package com.github.privacystreams.core.commons.time;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access time-related functions
 */

public class Times {

    /**
     * A predicate that checks whether the timestamp field value in a item is since a given timestamp
     * @param timestampField the name of the timestamp field
     * @param timestampToCompare the timestamp value to compare
     * @return the predicate
     */
    public static Function<Item, Boolean> since(final String timestampField, final Long timestampToCompare) {
        return new TimeSincePredicate(timestampField, timestampToCompare);
    }

    /**
     * A predicate that checks whether the timestamp field value in a item is a given duration recent from now
     * @param timestampField the name of the timestamp field
     * @param duration the millisecond duration from now
     * @return the predicate
     */
    public static Function<Item, Boolean> recent(final String timestampField, final Long duration) {
        return new TimeRecentPredicate(timestampField, duration);
    }
}
