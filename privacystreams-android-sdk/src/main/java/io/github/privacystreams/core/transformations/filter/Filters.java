package io.github.privacystreams.core.transformations.filter;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamTransformation;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access filter functions
 */
@PSOperatorWrapper
public class Filters {

    /**
     * Keep all items that satisfies a condition, and remove the items that don't satisfy.
     *
     * @param condition the function to check whether an item should be kept
     * @return the filter function
     */
    public static PStreamTransformation keep(Function<Item, Boolean> condition) {
        return new PredicateFilter(condition);
    }

    /**
     * Only keep the items that are different from the previous ones in the stream.
     * Eg. a stream [1, 1, 2, 2, 2, 1, 1] will be [1, 2, 1] after `keepChanges()`
     *
     * @return the filter function
     */
    public static PStreamTransformation keepChanges() {
        return new KeepChangesFilter();
    }

    /**
     * Only Keep the items whose fields are different from the previous ones in the stream.
     * Similar to `keepChanges()`, but only monitor a certain field
     *
     * @param fieldName the name of field to check whether an item should be kept
     * @return the filter function
     */
    public static PStreamTransformation keepChanges(String fieldName) {
        return new KeepFieldChangesFilter(fieldName);
    }

    /**
     * Sample the items based on a given interval. The items sent within the time interval
     * since last item are dropped.
     * Eg. If a stream has items sent at 1ms, 3ms, 7ms, 11ms and 40ms,
     * `sampleByInterval(10)` will only keep the items sent at 1ms, 11ms and 40ms.
     *
     * @param minInterval the minimum interval (in milliseconds) between each two items
     * @return the filter function
     */
    public static PStreamTransformation sampleByInterval(long minInterval) {
        return new SampleByIntervalFilter(minInterval);
    }

    /**
     * Sample the items based on a given step count. The items are filtered to make sure
     * `stepCount` number of items are dropped between each two new items.
     * Eg. `sampleByCount(2)` will keep the 1st, 4th, 7th, 10th, ... items
     *
     * @param stepCount the num of items to drop since last item
     * @return the filter function
     */
    public static PStreamTransformation sampleByCount(int stepCount) {
        return new SampleByCountFilter(stepCount);
    }
}
