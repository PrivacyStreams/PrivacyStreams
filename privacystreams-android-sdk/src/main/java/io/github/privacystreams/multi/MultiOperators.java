package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MultiOperators {
    public static Function<Item, Object> getItemField(int itemIndex, String itemField) {
        return new MultiItemGetField(itemIndex, itemField);
    }

    public static Function<Item, List<Object>> getLogItemField(int itemIndex, String itemField) {
        return new MultiItemGetLogField(itemIndex, itemField);
    }

    public static Function<Item, Object> getLogIndexItemField(int itemIndex, int indexInLog, String itemField){
        return new MultiItemGetLogIndexField(itemIndex, indexInLog, itemField);
    }

    public static Function<Item, Object> getLogFirstItemField(int itemIndex, String itemField){
        return new MultiItemGetLogIndexField(itemIndex, 0, itemField);
    }

    public static Function<Item, Object> getLogLastItemField(int itemIndex, String itemField){
        return new MultiItemGetLogIndexField(itemIndex, -1, itemField);
    }

    public static Function<Item, List<Object>> transformList(int logItemIndex, Function operator){
        return new MultiItemTransformList(logItemIndex, operator);
    }
}


