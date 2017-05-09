package com.github.privacystreams.commons.string;

import com.github.privacystreams.utils.Assertions;

/**
 * A function that gets the index of a certain substring.
 */
final class StringIndexOfFunction extends StringProcessor<Integer> {

    private final String searchString;

    StringIndexOfFunction(String stringField, String searchString) {
        super(stringField);
        this.searchString = Assertions.notNull("searchString", searchString);
        this.addParameters(searchString);
    }

    @Override
    protected Integer processString(String stringValue) {
        if (stringValue == null) return -1;
        return stringValue.indexOf(this.searchString);
    }
}
