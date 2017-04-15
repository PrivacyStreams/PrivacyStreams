package com.github.privacystreams.commons.string;

import com.github.privacystreams.utils.Assertions;

/**
 * Check whether the string specified by a field contains a certain substring.
 */
final class StringContainFunction extends StringProcessor<Boolean> {

    private final String searchString;

    StringContainFunction(String stringField, String searchString) {
        super(stringField);
        this.searchString = Assertions.notNull("searchString", searchString);
        this.addParameters(searchString);
    }

    @Override
    protected Boolean processString(String stringValue) {
        return stringValue != null && stringValue.contains(this.searchString);
    }
}
