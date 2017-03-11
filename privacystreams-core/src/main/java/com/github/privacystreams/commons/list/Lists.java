package com.github.privacystreams.commons.list;

import java.util.Arrays;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access list-related functions
 */

public class Lists {
    /**
     * A predicate that checks whether the list field value in a item contains a given value
     * @param listField the name of the list field
     * @param value the value to check if is in the list
     * @return the predicate
     */
    public static Function<Item, Boolean> contains(final String listField, final Object value) {
        return new ListContainsPredicate(listField, value);
    }

    /**
     * A predicate that checks whether the list field value in a item has intersection with a given list
     * @param listField the name of the list field
     * @param listToCompare the list to compare
     * @return the predicate
     */
    public static <TValue> Function<Item, Boolean> intersects(final String listField, final TValue[] listToCompare) {
        return new ListContainsPredicate(listField, Arrays.asList(listToCompare));
    }
}
