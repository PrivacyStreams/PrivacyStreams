package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 30/12/2016.
 * A helper class to access reorder functions
 */
public class Reorders {
    /**
     * A function that sorts the items in stream by the value of a field.
     * @param fieldToSort the name of the field to reorder by.
     * @return the function.
     */
    public static M2MTransformation sortBy(String fieldToSort) {
        return new ByFieldStreamSorter(fieldToSort);
    }

    /**
     * A function that shuffles the order items in stream.
     * @return the function.
     */
    public static M2MTransformation shuffle() {
        return new StreamShuffler();
    }

    /**
     * A function that reverses the order of items in stream.
     * @return the function.
     */
    public static M2MTransformation reverse() {
        return new StreamReverser();
    }
}
