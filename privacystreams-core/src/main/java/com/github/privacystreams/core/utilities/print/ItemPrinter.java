package com.github.privacystreams.core.utilities.print;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemFunction;

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

    @Override
    protected List<Object> getParameters() {
        return new ArrayList<>();
    }
}
