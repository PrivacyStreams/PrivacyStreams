package io.github.privacystreams.multi;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MultiOperators {
    public static Function<Item, Object> getItemField(int itemIndex, String itemField) {
        return new MultiItemGetField(itemIndex, itemField);
    }

    public static Function<Item, Object> getLogItemField(int itemIndex, String itemField) {
        return new MultiItemGetLogField(itemIndex, itemField);
    }
}


