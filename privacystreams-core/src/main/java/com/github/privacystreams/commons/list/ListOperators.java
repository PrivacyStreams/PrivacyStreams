package com.github.privacystreams.commons.list;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.Arrays;

/**
 * A helper class to access list-related functions.
 */
@PSOperatorWrapper
public class ListOperators {
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
