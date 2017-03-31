package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.transformations.group.GroupItem;
import com.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Output the sub stream field in an item with a function.
 */

class ItemSubStreamFunction<Tout> extends ItemFunction<Tout> {
    private Function<List<Item>, Tout> subStreamFunction;

    ItemSubStreamFunction(Function<List<Item>, Tout> subStreamFunction) {
        this.subStreamFunction = Assertions.notNull("subStreamFunction", subStreamFunction);
        this.addParameters(subStreamFunction);
    }

    @Override
    public Tout apply(UQI uqi, Item input) {
        List<Item> subStreamItems = input.getValueByField(GroupItem.GROUPED_ITEMS);
        List<Item> items = new ArrayList<>();
        items.addAll(subStreamItems);
        return this.subStreamFunction.apply(uqi, items);
    }
}
