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
}
