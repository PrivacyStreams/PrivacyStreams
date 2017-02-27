package com.github.privacystreams.core.commons.print;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 30/12/2016.
 * A helper class to access print-related functions
 */

public class Printers {
    /**
     * A function that prints an item to stdout
     * @return the function
     */
    public static Function<Item, Void> print() {
        return new ItemPrinter();
    }

    /**
     * A function that prints an item for debugging
     * @return the function
     */
    public static Function<Item, Void> debug() {
        return new ItemDebugPrinter();
    }

}
