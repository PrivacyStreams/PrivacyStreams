package com.github.privacystreams.commons.stream;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access common stream functions
 */
@PSOperatorWrapper
public class StreamOperators {

    /**
     * Collect the items in the stream to a list.
     * Each element in the list is an instance of Item.
     *
     * @return the function
     */
    public static Function<List<Item>, List<Item>> asList() {
        return new StreamListCollector();
    }

    /**
     * Collect the values of a field in the stream to a list.
     * Each element in the list is the value of the specified field in an item.
     *
     * @param fieldToCollect the name of the field to collect.
     * @return the function
     */
    public static <TValue> Function<List<Item>, List<TValue>> asList(String fieldToCollect) {
        return new StreamFieldListCollector<>(fieldToCollect);
    }

}
