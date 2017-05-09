package com.github.privacystreams.commons.string;

/**
 * Get the substring of the string specified by a field in the item.
 */
final class SubStringFunction extends StringProcessor<String> {

    private final int start;
    private final int end;

    SubStringFunction(String stringField, int start, int end) {
        super(stringField);
        this.start = start;
        this.end = end;
        this.addParameters(start, end);
    }

    @Override
    protected String processString(String stringValue) {
        if (stringValue == null) return null;
        return stringValue.substring(this.start, this.end);
    }
}
