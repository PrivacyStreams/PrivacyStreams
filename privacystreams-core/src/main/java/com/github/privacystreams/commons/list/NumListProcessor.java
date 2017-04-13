package com.github.privacystreams.commons.list;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

import java.util.List;

/**
 * Process the list field in an item.
 */
abstract class NumListProcessor<Tout> extends ItemFunction<Tout> {

    private final String numListField;

    NumListProcessor(String numListField) {
        this.numListField = Assertions.notNull("numListField", numListField);
        this.addParameters(numListField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        List<Number> numList = input.getValueByField(this.numListField);
        return this.processNumList(numList);
    }

    protected abstract Tout processNumList(List<Number> numList);

}
