package com.github.privacystreams.core.transformations.pick;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.SStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access picker functions
 */
@PSOperatorWrapper
public class Pickers {
    /**
     * Pick an item from a stream.
     *
     * @param index the index of the item to pick.
     * @return the stream mapper function.
     */
    public static Function<MStream, SStream> pick(int index) {
        return new StreamItemPicker(index);
    }
}
