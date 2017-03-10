package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access reorder functions.
 */
@PSOperatorWrapper
public class Reorders {
    /**
     * A function that sorts the items in stream by the value of a field.
     *
     * @param fieldToSort the name of the field to reorder by.
     * @return the function.
     */
    public static Function<MultiItemStream, MultiItemStream> sortBy(String fieldToSort) {
        return new ByFieldStreamSorter(fieldToSort);
    }

    /**
     * A function that randomizes the order of items in stream.
     *
     * @return the function.
     */
    public static Function<MultiItemStream, MultiItemStream> shuffle() {
        return new StreamShuffler();
    }

    /**
     * A function that reverses the order of items in stream.
     *
     * @return the function.
     */
    public static Function<MultiItemStream, MultiItemStream> reverse() {
        return new StreamReverser();
    }
}
