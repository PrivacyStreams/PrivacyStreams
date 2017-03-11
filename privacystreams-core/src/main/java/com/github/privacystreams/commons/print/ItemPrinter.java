package com.github.privacystreams.commons.print;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;

/**
 * Created by yuanchun on 27/12/2016.
 * Print the item
 */

final class ItemPrinter extends ItemFunction<Void> {

    @Override
    public Void apply(UQI uqi, Item input) {
        System.out.println(input);
        return null;
    }

}
