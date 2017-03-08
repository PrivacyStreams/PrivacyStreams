package com.github.privacystreams.commons.string;

import com.github.privacystreams.core.utils.Assertions;

import org.apache.commons.lang3.StringUtils;

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
        return StringUtils.indexOf(stringValue, this.searchString);
    }
}
