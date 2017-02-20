package com.github.privacystreams.core.transformations.pick;

import com.github.privacystreams.core.transformations.M2STransformation;

/**
 * Created by yuanchun on 29/01/2017.
 * A helper class to access picker functions
 */

public class Pickers {
    /**
     * A function that picks an item from a stream
     * @param index the mapper function to map each item in the stream.
     * @return the stream mapper function.
     */
    public static M2STransformation pick(int index) {
        return new StreamItemPicker(index);
    }

    /**
     * A function that picks the first item from a stream
     * @return the stream mapper function.
     */
    public static M2STransformation first() {
        return new StreamItemPicker(0);
    }
}
