package com.github.privacystreams.commons.string;

/**
 * Get the substring of the string specified by a field in the item.
 */
final class StringElideFunction extends StringProcessor<String> {

    private final int start;
    private final int end;

    StringElideFunction(String stringField, int start, int end) {
        super(stringField);
        this.start = start;
        this.end = end;
        this.addParameters(start, end);
    }

    @Override
    protected String processString(String stringValue) {
        if (stringValue == null) return null;
        int start = this.start;
        int end = this.end;
        if (start > end) return stringValue;
        if (start < 0) start = 0;
        if (end > stringValue.length()) end = stringValue.length() - 1;
        int elideLen = end - start + 1;
        String elideStr = String.format("%0" + elideLen + "d", 0).replace("0", "*");
        return stringValue.substring(0, start) + elideStr + stringValue.substring(end + 1);
    }
}
