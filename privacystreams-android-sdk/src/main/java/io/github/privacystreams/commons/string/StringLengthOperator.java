package io.github.privacystreams.commons.string;

/**
 * Calculate the length of a string field in the item.
 */
final class StringLengthOperator extends StringProcessor<Integer> {

    StringLengthOperator(String stringField) {
        super(stringField);
    }

    @Override
    protected Integer processString(String stringValue) {
        return stringValue == null ? 0 : stringValue.length();
    }
}
