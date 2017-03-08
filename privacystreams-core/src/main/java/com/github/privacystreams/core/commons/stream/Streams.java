package com.github.privacystreams.core.commons.stream;

import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access common stream functions
 */

public class Streams {

    /**
     * A function that converts a stream to a list.
     * Each element in the list is an item map.
     *
     * @return the function
     */
    public static Function<List<Item>, List<Item>> asList() {
        return new StreamListCollector();
    }

    /**
     * A function that converts a stream to a list.
     * Each element in the list is the value of a selected field.
     *
     * @param fieldToSelect the name of the field to select.
     * @return the function
     */
    public static <TValue> Function<List<Item>, List<TValue>> asList(String fieldToSelect) {
        return new StreamFieldListCollector<>(fieldToSelect);
    }

}
