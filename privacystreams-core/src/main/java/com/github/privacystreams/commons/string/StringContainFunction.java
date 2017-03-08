package com.github.privacystreams.commons.string;

import com.github.privacystreams.core.utils.Assertions;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yuanchun on 30/12/2016.
 * A function that checks whether a string field in the item contains a certain substring
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
        return StringUtils.contains(stringValue, this.searchString);
    }
}
