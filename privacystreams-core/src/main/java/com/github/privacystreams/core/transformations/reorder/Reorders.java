package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access reorder functions.
 */
@PSOperatorWrapper
public class Reorders {
    /**
     * Sort the items in stream by the value of a field.
     *
     * @param fieldToSort the name of the field to reorder by.
     * @return the function.
     */
    public static Function<MStream, MStream> sortBy(String fieldToSort) {
        return new ByFieldStreamSorter(fieldToSort);
    }

    /**
     * Shuffle the order of the items in stream.
     *
     * @return the function.
     */
    public static Function<MStream, MStream> shuffle() {
        return new StreamShuffler();
    }

    /**
     * Reverse the order of the items in stream.
     *
     * @return the function.
     */
    public static Function<MStream, MStream> reverse() {
        return new StreamReverser();
    }
}
