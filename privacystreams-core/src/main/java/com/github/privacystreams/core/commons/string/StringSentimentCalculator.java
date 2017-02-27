package com.github.privacystreams.core.commons.string;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that calculate the sentiment of a given string
 */
final class StringSentimentCalculator extends StringProcessor<Double> {

    StringSentimentCalculator(String stringField) {
        super(stringField);
    }

    @Override
    protected Double processString(String stringValue) {
        // TODO calculate sentiment score of given string
        return null;
    }

}
