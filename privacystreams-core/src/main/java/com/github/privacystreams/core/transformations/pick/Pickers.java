package com.github.privacystreams.core.transformations.pick;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * A helper class to access picker functions
 */

public class Pickers {
    /**
     * A function that picks an item from a stream
     * @param index the mapper function to map each item in the stream.
     * @return the stream mapper function.
     */
    public static Function<MultiItemStream, SingleItemStream> pick(int index) {
        return new StreamItemPicker(index);
    }
}
