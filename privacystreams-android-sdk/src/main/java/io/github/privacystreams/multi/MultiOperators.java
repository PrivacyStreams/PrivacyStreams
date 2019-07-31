package io.github.privacystreams.multi;


import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

@PSOperatorWrapper
public class MultiOperators {
    public static Function<Item, Object> getField(String itemField) {
        return new GetField(itemField);
    }
}


