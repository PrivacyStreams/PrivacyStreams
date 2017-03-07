package com.github.privacystreams.core.commons.logic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access logic-related functions
 */

public class Logics {
    /**
     * A predicate that returns the logical NOT of a function.
     * @param predicate the function to test an item
     * @return the function
     */
    public static Function<Item, Boolean> not(final Function<Item, Boolean> predicate) {
        return new NotFunction(predicate);
    }

    /**
     * A predicate that returns the logical AND of two functions.
     * @param predicate1 the first function to test an item
     * @param predicate2 the second function to test an item
     * @return the function
     */
    public static Function<Item, Boolean> and(final Function<Item, Boolean> predicate1, final Function<Item, Boolean> predicate2) {
        return new AndFunction(predicate1, predicate2);
    }

    /**
     * A predicate that returns the logical OR of two functions.
     * @param predicate1 the first function to test an item
     * @param predicate2 the second function to test an item
     * @return the function
     */
    public static Function<Item, Boolean> or(final Function<Item, Boolean> predicate1, final Function<Item, Boolean> predicate2) {
        return new OrFunction(predicate1, predicate2);
    }
}
