package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access grouping functions
 */
@PSOperatorWrapper
public class Groupers {

    /**
     * Group the items with same field value together.
     * This function will change the order of items.
     * The stream after grouping is a stream of GroupItem items.
     *
     * @param fieldToGroup the field used to group the stream.
     * @return the grouper function.
     */
    public static Function<MultiItemStream, MultiItemStream> groupBy(String fieldToGroup) {
        return new ByFieldGrouper(fieldToGroup);
    }

    /**
     * Group the contiguous items with same field value together.
     * This function will not change the order of items.
     * The stream after grouping is a stream of GroupItem items.
     *
     * @param fieldToGroup the field used to group the stream.
     * @return the grouper function.
     */
    public static Function<MultiItemStream, MultiItemStream> localGroupBy(String fieldToGroup) {
        return new ByFieldLocalGrouper(fieldToGroup);
    }

}
