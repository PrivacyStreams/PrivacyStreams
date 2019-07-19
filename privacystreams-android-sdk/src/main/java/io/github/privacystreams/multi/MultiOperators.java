package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MultiOperators {
    public static Function<Item, Object> getField(ItemType.iType type, String itemField){
        return new NewMultiItemGetField(type, itemField);
    }

    public static Function<Item, Object> getField(String itemField) {
        return new GetField(itemField);
    }

    public static Function<Item, List<Object>> transformList(ItemType.iType type, Function operator){
        return new NewTransformList(type, operator);
    }

    public static Function<Item, Object> getIndexFromList(String itemField, int index){
        return new GetIndex(itemField, index);
    }
}


