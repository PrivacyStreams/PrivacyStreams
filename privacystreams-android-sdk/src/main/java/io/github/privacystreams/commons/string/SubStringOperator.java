package io.github.privacystreams.commons.string;

/**
 * Get the substring of the string specified by a field in the item.
 */
final class SubStringOperator extends StringProcessor<String> {

    private final int start;
    private final int end;

    SubStringOperator(String stringField, int start, int end) {
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
