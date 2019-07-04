package io.github.privacystreams.communication;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;

public class TFIDFOperators {

    public static Function<Item, String> getEmotion(String messageDataField){
        return new SentimentAnalyzer(messageDataField);
    }
}
