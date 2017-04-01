package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.transformations.M2MTransformation;
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
    public static M2MTransformation sortBy(String fieldToSort) {
        return new ByFieldStreamSorter(fieldToSort);
    }

    /**
     * Shuffle the order of the items in stream.
     *
     * @return the function.
     */
    public static M2MTransformation shuffle() {
        return new StreamShuffler();
    }

    /**
     * Reverse the order of the items in stream.
     *
     * @return the function.
     */
    public static M2MTransformation reverse() {
        return new StreamReverser();
    }
}
