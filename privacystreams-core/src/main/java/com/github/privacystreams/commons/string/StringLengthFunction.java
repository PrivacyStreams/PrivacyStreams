package com.github.privacystreams.commons.string;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that calculates the length of a string field in the item
 */
final class StringLengthFunction extends StringProcessor<Integer> {

    StringLengthFunction(String stringField) {
        super(stringField);
    }

    @Override
    protected Integer processString(String stringValue) {
        return StringUtils.length(stringValue);
    }
}
