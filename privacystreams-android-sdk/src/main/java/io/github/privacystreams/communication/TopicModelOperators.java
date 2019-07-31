package io.github.privacystreams.communication;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;

public class TopicModelOperators {

    public static Function<Item, String> getCategories(String messageDataField){
        return new TopicModelAnalyzer(messageDataField);
    }
}
