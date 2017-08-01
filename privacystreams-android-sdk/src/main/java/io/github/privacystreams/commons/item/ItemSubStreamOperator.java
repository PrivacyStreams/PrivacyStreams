package io.github.privacystreams.commons.item;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.transformations.group.GroupItem;
import io.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Output the sub stream field in an item with a function.
 */

class ItemSubStreamOperator<Tout> extends ItemOperator<Tout> {
    private Function<List<Item>, Tout> subStreamFunction;

    ItemSubStreamOperator(Function<List<Item>, Tout> subStreamFunction) {
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
