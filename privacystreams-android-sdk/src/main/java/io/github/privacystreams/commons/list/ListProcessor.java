package io.github.privacystreams.commons.list;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

import java.util.List;

/**
 * Process the list field in an item.
 */
abstract class ListProcessor<Tout> extends ItemOperator<Tout> {

    private final String listField;

    ListProcessor(String listField) {
        this.listField = Assertions.notNull("listField", listField);
        this.addParameters(listField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        List<Object> list = input.getValueByField(this.listField);
        return this.processList(list);
    }

    protected abstract Tout processList(List<Object> list);

}
