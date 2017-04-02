package com.github.privacystreams.core.transformations.select;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2STransformation;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access selector functions
 */
@PSOperatorWrapper
public class Selectors {
    /**
     * Select an item from a stream by a given index.
     *
     * @param index the index of the item to pick.
     * @return the stream mapper function.
     */
    public static M2STransformation getItemAt(int index) {
        return new StreamItemPicker(index);
    }

    /**
     * Select an item from a stream with a function.
     *
     * @param selector the function to select the item.
     * @return the stream mapper function.
     */
    public static M2STransformation select(Function<List<Item>, Item> selector) {
        return new WithFunctionSelector(selector);
    }
}
