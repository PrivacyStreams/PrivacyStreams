package com.github.privacystreams.commons.string;

import org.apache.commons.lang3.StringUtils;

/**
 * Calculate the length of a string field in the item.
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
