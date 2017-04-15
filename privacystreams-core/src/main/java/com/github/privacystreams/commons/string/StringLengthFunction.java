package com.github.privacystreams.commons.string;

/**
 * Calculate the length of a string field in the item.
 */
final class StringLengthFunction extends StringProcessor<Integer> {

    StringLengthFunction(String stringField) {
        super(stringField);
    }

    @Override
    protected Integer processString(String stringValue) {
        return stringValue == null ? 0 : stringValue.length();
    }
}
