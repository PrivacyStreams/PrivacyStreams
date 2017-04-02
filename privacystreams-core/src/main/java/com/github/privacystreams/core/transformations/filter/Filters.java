package com.github.privacystreams.core.transformations.filter;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

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
    public static M2MTransformation keep(Function<Item, Boolean> condition) {
        return new PredicateFilter(condition);
    }
}
