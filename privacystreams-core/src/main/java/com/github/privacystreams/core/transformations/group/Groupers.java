package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access grouping functions
 */
@PSOperatorWrapper
public class Groupers {

    /**
     * Group together the items with same field value.
     * This function will change the order of items.
     * The stream after grouping is a stream of GroupItem items.
     *
     * @param fieldToGroup the field used to group the stream.
     * @return the grouper function.
     */
    public static M2MTransformation groupBy(String fieldToGroup) {
        return new ByFieldGrouper(fieldToGroup);
    }

    /**
     * Group together the *contiguous* items with same field value.
     * This function will not change the order of items.
     * The stream after grouping is a stream of GroupItem items.
     *
     * @param fieldToGroup the field used to group the stream.
     * @return the grouper function.
     */
    public static M2MTransformation localGroupBy(String fieldToGroup) {
        return new ByFieldLocalGrouper(fieldToGroup);
    }

}
